package com.revolgenx.anilib.ui.bottomsheet.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.google.android.flexbox.FlexboxLayoutManager
import com.pranavpandey.android.dynamic.support.model.DynamicMenu
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.loadMediaListFilter
import com.revolgenx.anilib.data.meta.MediaListFilterMeta
import com.revolgenx.anilib.data.meta.type.ALMediaListCollectionSort
import com.revolgenx.anilib.databinding.MediaListFilterBottomSheetLayoutBinding
import com.revolgenx.anilib.infrastructure.event.MediaListCollectionFilterEvent
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.ui.adapter.MediaFilterFormatAdapter
import com.revolgenx.anilib.ui.bottomsheet.DynamicBottomSheetFragment
import com.revolgenx.anilib.ui.dialog.FormatSelectionDialog
import com.revolgenx.anilib.ui.dialog.sorting.AniLibSortingModel
import com.revolgenx.anilib.ui.dialog.sorting.SortOrder
import com.revolgenx.anilib.ui.view.makeSpinnerAdapter

class MediaListFilterBottomSheetFragment :
    DynamicBottomSheetFragment<MediaListFilterBottomSheetLayoutBinding>() {


    override val positiveText: Int
        get() = R.string.done

    override val negativeText: Int
        get() = R.string.cancel


    private val adapter by lazy {
        MediaFilterFormatAdapter(requireContext())
    }

    private var formatDialog: DialogFragment? = null

    companion object {
        const val LIST_FILTER_PARCEL_KEY = "LIST_FILTER_PARCEL_KEY"
        const val LIST_FILTER_MEDIA_TYPE_KEY = "LIST_FILTER_MEDIA_TYPE_KEY"
    }


    fun show(
        ctx: Context,
        type: Int,
        func: MediaListFilterBottomSheetFragment.() -> Unit
    ): MediaListFilterBottomSheetFragment {
        this.windowContext = ctx
        arguments = bundleOf(LIST_FILTER_MEDIA_TYPE_KEY to type)
        this.func()
        this.show()
        return this
    }

    private val mediaType get() = arguments?.getInt(LIST_FILTER_MEDIA_TYPE_KEY) ?: 0

    private val field by lazy {
        arguments?.getParcelable(LIST_FILTER_PARCEL_KEY) ?: loadMediaListFilter(
            requireContext(),
            mediaType
        )
    }


    override fun bindView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): MediaListFilterBottomSheetLayoutBinding {
        return MediaListFilterBottomSheetLayoutBinding.inflate(layoutInflater(), container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        binding.updateView(savedInstanceState)


        onPositiveClicked = {
            MediaListFilterMeta(
                formatsIn = field.formatsIn,
                status = (binding.listStatusSpinner.selectedItemPosition - 1).takeIf { it >= 0 },
                genres = binding.listGenreSpinner.let { spin ->
                    (spin.selectedItemPosition - 1).takeIf { it >= 0 }
                        ?.let { (spin.selectedItem as DynamicMenu).title.toString() }
                },
                mediaListSort = getActiveListSort()
            ).let {
                MediaListCollectionFilterEvent(it).postEvent
            }
            dismiss()
        }

        onNegativeClicked = {
            dismiss()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        binding.let {
            field.status = (binding.listStatusSpinner.selectedItemPosition - 1).takeIf { it >= 0 }
            field.genre = binding.listGenreSpinner.let { spin ->
                (spin.selectedItemPosition - 1).takeIf { it >= 0 }
                    ?.let { (spin.selectedItem as DynamicMenu).title.toString() }
            }
            field.listSort = getActiveListSort()
        }
        super.onSaveInstanceState(outState)
    }

    private fun getActiveListSort(): Int? {
        return binding.listSortLayout.getActiveSortItem()?.let {
            var activeSort = (it.data as ALMediaListCollectionSort).sort
            if (it.order == SortOrder.DESC) {
                activeSort += 1
            }
            activeSort
        }
    }


    private fun MediaListFilterBottomSheetLayoutBinding.updateView(savedInstanceState: Bundle?) {
        val listStatusItems =
            requireContext().resources.getStringArray(R.array.advance_search_status).map {
                DynamicMenu(
                    null, it
                )
            }

        val genre = requireContext().resources.getStringArray(R.array.media_genre)
        val listGenreItems = mutableListOf<DynamicMenu>().apply {
            add(DynamicMenu(null, getString(R.string.none)))
            addAll(genre.map {
                DynamicMenu(null, it)
            })
        }

        formatAddHeader.setOnClickListener {
            formatDialog = FormatSelectionDialog.newInstance(field.formatsIn!!).also {
                it.onDoneListener = {
                    field.formatsIn = it.toMutableList()
                    bindFormat()
                }
                it.show(childFragmentManager, FormatSelectionDialog::class.java.simpleName)
            }
        }

        listStatusSpinner.adapter = makeSpinnerAdapter(requireContext(), listStatusItems)
        listGenreSpinner.adapter = makeSpinnerAdapter(requireContext(), listGenreItems)


        if (mediaType != MediaType.ANIME.ordinal) {
            formatAddHeader.visibility = View.GONE
            formatChipRecyclerView.visibility = View.GONE
        } else {
            field.formatsIn = field.formatsIn ?: mutableListOf()
            binding.formatChipRecyclerView.layoutManager = FlexboxLayoutManager(requireContext())
            bindFormat()
        }

        field.status?.let { listStatusSpinner.setSelection(it + 1) }
        field.genre?.let { listGenreSpinner.setSelection(genre.indexOf(it) + 1) }


        var savedSortOrder: SortOrder = SortOrder.NONE
        val alMediaListSort = requireContext().resources.getStringArray(R.array.al_media_list_collection_sort)
        val alMediaListSortEnums = ALMediaListCollectionSort.values()
        var savedSortIndex = -1

        field.listSort?.takeIf { it > -1 }?.let { saveSort ->
            savedSortOrder = if (saveSort % 2 == 0) {
                savedSortIndex = alMediaListSortEnums.first { it.sort == saveSort }.ordinal
                SortOrder.ASC
            } else {
                savedSortIndex = alMediaListSortEnums.first { it.sort == saveSort - 1 }.ordinal
                SortOrder.DESC
            }
        }

        val alMediaListSortModels = alMediaListSort.mapIndexed { index, sort ->
            AniLibSortingModel(
                alMediaListSortEnums[index],
                sort,
                if (savedSortIndex == index) savedSortOrder else SortOrder.NONE
            )
        }

        binding.listSortLayout.setSortItems(alMediaListSortModels)
    }

    override fun onStop() {
        super.onStop()
        formatDialog?.dismissAllowingStateLoss()
        formatDialog = null
    }


    private fun MediaListFilterBottomSheetLayoutBinding.bindFormat() {
        formatChipRecyclerView.adapter = adapter
        adapter.currentList = field.formatsIn!!
    }

}