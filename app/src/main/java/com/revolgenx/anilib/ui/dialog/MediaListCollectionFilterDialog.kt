package com.revolgenx.anilib.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import androidx.core.os.bundleOf
import com.pranavpandey.android.dynamic.support.adapter.DynamicSpinnerImageAdapter
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.pranavpandey.android.dynamic.support.model.DynamicSpinnerItem
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.dialog.BaseDialogFragment
import com.revolgenx.anilib.app.theme.ThemeController
import com.revolgenx.anilib.infrastructure.event.MediaListCollectionFilterEvent
import com.revolgenx.anilib.data.field.MediaListCollectionFilterField
import com.revolgenx.anilib.data.meta.MediaListFilterMeta
import com.revolgenx.anilib.databinding.ListCollectionFilterDialogLayoutBinding


class MediaListCollectionFilterDialog : BaseDialogFragment() {
    companion object {
        fun newInstance(mediaListFilterField: MediaListCollectionFilterField) = MediaListCollectionFilterDialog().also {
            it.arguments = bundleOf(LIST_FILTER_PARCEL_KEY to mediaListFilterField)
        }

        private const val LIST_FORMAT_KEY = "LIST_FORMAT_KEY"
        private const val LIST_STATUS_KEY = "LIST_STATUS_KEY"
        private const val LIST_GENRE_KEY = "LIST_GENRE_KEY"
        private const val LIST_MEDIA_LIST_SORT_KEY = "LIST_MEDIA_LIST_SORT_KEY"
        const val LIST_FILTER_PARCEL_KEY = "LIST_FILTER_PARCEL_KEY"
    }

    override var titleRes: Int? = R.string.filter
    override var viewRes: Int? = R.layout.list_collection_filter_dialog_layout
    override var positiveText: Int? = R.string.done
    override var negativeText: Int? = R.string.cancel


    override fun onPositiveClicked(dialogInterface: DialogInterface, which: Int) {
        val binding = ListCollectionFilterDialogLayoutBinding.bind(dialogView)
        if (dialogInterface is DynamicDialog) {
            MediaListFilterMeta(
                format = (binding.listFormatSpinner.selectedItemPosition - 1).takeIf { it >= 0 },
                status = (binding.listStatusSpinner.selectedItemPosition - 1).takeIf { it >= 0 },
                genres = binding.listGenreSpinner.let { spin ->
                    (spin.selectedItemPosition - 1).takeIf { it >= 0 }
                        ?.let { (spin.selectedItem as DynamicSpinnerItem).text.toString() }
                },
                mediaListSort =  (binding.listSortSpinner.selectedItemPosition - 1).takeIf { it >= 0 }
            ).let {
                MediaListCollectionFilterEvent(it).postEvent
            }
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        val binding = ListCollectionFilterDialogLayoutBinding.bind(dialogView)
        binding.let {
            outState.putInt(LIST_FORMAT_KEY, it.listFormatSpinner.selectedItemPosition)
            outState.putInt(LIST_STATUS_KEY, it.listStatusSpinner.selectedItemPosition)
            outState.putInt(LIST_GENRE_KEY, it.listGenreSpinner.selectedItemPosition)
            outState.putInt(LIST_MEDIA_LIST_SORT_KEY, it.listSortSpinner.selectedItemPosition)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onShowListener(alertDialog: DynamicDialog, savedInstanceState: Bundle?) {
        val binding = ListCollectionFilterDialogLayoutBinding.bind(dialogView)

        with(binding){
            this.updateTheme()
            this.updateView(savedInstanceState)
        }
    }


    private fun ListCollectionFilterDialogLayoutBinding.updateTheme() {
        ThemeController.lightSurfaceColor().let {
            listFormatFrameLayout.setBackgroundColor(it)
            listStatusFrameLayout.setBackgroundColor(it)
            listGenreFrameLayout.setBackgroundColor(it)
            listSortFrameLayout.setBackgroundColor(it)
        }
    }

    private fun ListCollectionFilterDialogLayoutBinding.updateView(savedInstanceState: Bundle?) {
        val listFormatItems = requireContext().resources.getStringArray(R.array.advance_search_format).map {
            DynamicSpinnerItem(
                null, it
            )
        }
        val listStatusItems = requireContext().resources.getStringArray(R.array.advance_search_status).map {
            DynamicSpinnerItem(
                null, it
            )
        }

        val genre = requireContext().resources.getStringArray(R.array.media_genre)
        val listGenreItems = mutableListOf<DynamicSpinnerItem>().apply {
            add(DynamicSpinnerItem(null, getString(R.string.none)))
            addAll(genre.map {
                DynamicSpinnerItem(null, it)
            })
        }

        val mediaListSort = requireContext().resources.getStringArray(R.array.media_list_collection_sort)
        val mediaListSortItems = mutableListOf<DynamicSpinnerItem>().apply {
            add(DynamicSpinnerItem(null, getString(R.string.none)))
            addAll(mediaListSort.map {
                DynamicSpinnerItem(null, it)
            })
        }

        listFormatSpinner.adapter = makeSpinnerAdapter(listFormatItems)
        listStatusSpinner.adapter = makeSpinnerAdapter(listStatusItems)
        listGenreSpinner.adapter = makeSpinnerAdapter(listGenreItems)
        listSortSpinner.adapter = makeSpinnerAdapter(mediaListSortItems)

        savedInstanceState?.let {
            listFormatSpinner.setSelection(it.getInt(LIST_FORMAT_KEY))
            listStatusSpinner.setSelection(it.getInt(LIST_STATUS_KEY))
            listGenreSpinner.setSelection(it.getInt(LIST_GENRE_KEY))
            listSortSpinner.setSelection(it.getInt(LIST_MEDIA_LIST_SORT_KEY))
        } ?: let {
            arguments?.getParcelable<MediaListCollectionFilterField>(LIST_FILTER_PARCEL_KEY)?.let {
                it.format?.let { listFormatSpinner.setSelection(it + 1) }
                it.status?.let { listStatusSpinner.setSelection(it + 1) }
                it.genre?.let { listGenreSpinner.setSelection(genre.indexOf(it) + 1) }
                it.listSort?.let { listSortSpinner.setSelection(it + 1) }
            }
        }

    }

    private fun makeSpinnerAdapter(items: List<DynamicSpinnerItem>) =
        DynamicSpinnerImageAdapter(
            requireContext(),
            R.layout.ads_layout_spinner_item,
            R.id.ads_spinner_item_icon,
            R.id.ads_spinner_item_text, items
        )

}