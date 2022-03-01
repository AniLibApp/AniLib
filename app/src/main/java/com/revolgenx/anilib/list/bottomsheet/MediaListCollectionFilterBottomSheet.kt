package com.revolgenx.anilib.list.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.google.android.flexbox.FlexboxLayoutManager
import com.pranavpandey.android.dynamic.support.model.DynamicMenu
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.bottomsheet.DynamicBottomSheetFragment
import com.revolgenx.anilib.constant.ALMediaListCollectionSort
import com.revolgenx.anilib.databinding.MediaListFilterBottomSheetLayoutBinding
import com.revolgenx.anilib.list.data.meta.MediaListCollectionFilterMeta
import com.revolgenx.anilib.ui.adapter.MediaFilterFormatAdapter
import com.revolgenx.anilib.ui.dialog.FormatSelectionDialog
import com.revolgenx.anilib.ui.dialog.sorting.AniLibSortingModel
import com.revolgenx.anilib.ui.dialog.sorting.SortOrder
import com.revolgenx.anilib.ui.view.makeSpinnerAdapter

class MediaListCollectionFilterBottomSheet :
    DynamicBottomSheetFragment<MediaListFilterBottomSheetLayoutBinding>() {

    private var formatDialog: DialogFragment? = null

    private val mediaListFilter by lazy {
        arguments?.getParcelable(media_list_collection_filter_meta_key)
            ?: MediaListCollectionFilterMeta()
    }

    private var onFilterMediaList: ((filter: MediaListCollectionFilterMeta) -> Unit)? = null

    companion object {
        private const val media_list_collection_filter_meta_key =
            "media_list_collection_filter_meta_key"

        fun newInstance(
            meta: MediaListCollectionFilterMeta,
            onFilterMediaList: (filter: MediaListCollectionFilterMeta) -> Unit
        ) =
            MediaListCollectionFilterBottomSheet().also {
                it.arguments = bundleOf(media_list_collection_filter_meta_key to meta)
                it.onFilterMediaList = onFilterMediaList
            }
    }


    override fun bindView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): MediaListFilterBottomSheetLayoutBinding {
        return MediaListFilterBottomSheetLayoutBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.updateView()

        onPositiveClicked = {
            with(mediaListFilter) {
                status = (binding.listStatusSpinner.selectedItemPosition - 1).takeIf { it >= 0 }
                genre = binding.listGenreSpinner.let { spin ->
                    (spin.selectedItemPosition - 1).takeIf { it >= 0 }
                        ?.let { (spin.selectedItem as DynamicMenu).title.toString() }
                }
                sort = getActiveListSort()
            }

            onFilterMediaList?.invoke(mediaListFilter)
        }
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


    private fun MediaListFilterBottomSheetLayoutBinding.updateView() {
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
            formatDialog = FormatSelectionDialog.newInstance(mediaListFilter.formatsIn!!).also {
                it.onDoneListener = {
                    mediaListFilter.formatsIn = it.toMutableList()
                    bindFormat()
                }
                it.show(childFragmentManager, FormatSelectionDialog::class.java.simpleName)
            }
        }

        listStatusSpinner.adapter = makeSpinnerAdapter(requireContext(), listStatusItems)
        listGenreSpinner.adapter = makeSpinnerAdapter(requireContext(), listGenreItems)


        mediaListFilter.formatsIn = mediaListFilter.formatsIn ?: mutableListOf()
        binding.formatChipRecyclerView.layoutManager = FlexboxLayoutManager(requireContext())
        bindFormat()

        mediaListFilter.status?.let { listStatusSpinner.setSelection(it + 1) }
        mediaListFilter.genre?.let { listGenreSpinner.setSelection(genre.indexOf(it) + 1) }


        var savedSortOrder: SortOrder = SortOrder.NONE
        val alMediaListSort =
            requireContext().resources.getStringArray(R.array.al_media_list_collection_sort)
        val alMediaListSortEnums = ALMediaListCollectionSort.values()
        var savedSortIndex = -1

        mediaListFilter.sort?.takeIf { it > -1 }?.let { saveSort ->
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
        formatChipRecyclerView.adapter =
            MediaFilterFormatAdapter(requireContext()).also { adapter ->
                adapter.currentList = mediaListFilter.formatsIn
            }
    }
}
