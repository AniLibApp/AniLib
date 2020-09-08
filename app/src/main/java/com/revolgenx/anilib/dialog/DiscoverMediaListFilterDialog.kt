package com.revolgenx.anilib.dialog

import android.content.DialogInterface
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.pranavpandey.android.dynamic.support.adapter.DynamicSpinnerImageAdapter
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.pranavpandey.android.dynamic.support.model.DynamicSpinnerItem
import com.revolgenx.anilib.R
import com.revolgenx.anilib.preference.getDiscoverMediaListSort
import com.revolgenx.anilib.preference.setDiscoverMediaListSort
import kotlinx.android.synthetic.main.media_list_filter_dialog_layout.*

class DiscoverMediaListFilterDialog : BaseDialogFragment() {
    override var positiveText: Int? = R.string.done
    override var negativeText: Int? = R.string.cancel
    override var viewRes: Int? = R.layout.media_list_filter_dialog_layout
    override var titleRes: Int? = R.string.filter


    var onDoneListener: (() -> Unit)? = null

    private val mediaListSortStrings by lazy {
        requireContext().resources.getStringArray(R.array.media_list_sort)
    }


    companion object {
        private const val MEDIA_LIST_FILTER_TYPE = "MEDIA_LIST_FILTER_TYPE"
        private const val MEDIA_LIST_SORT_KEY = "MEDIA_LIST_SORT_KEY"


        fun newInstance(type: Int): DiscoverMediaListFilterDialog {
            return DiscoverMediaListFilterDialog().apply {
                arguments = bundleOf(MEDIA_LIST_FILTER_TYPE to type)
            }
        }
    }

    override fun onShowListener(alertDialog: DynamicDialog, savedInstanceState: Bundle?) {
        with(alertDialog) {
            val type = arguments?.getInt(MEDIA_LIST_FILTER_TYPE)!!
            val mediaListSortItems = mutableListOf<DynamicSpinnerItem>().apply {
                add(DynamicSpinnerItem(null, getString(R.string.none)))
                addAll(mediaListSortStrings.map {
                    DynamicSpinnerItem(null, it)
                })
            }
            mediaListSortSpinner.adapter = makeSpinnerAdapter(mediaListSortItems)
            savedInstanceState?.let {
                mediaListSortSpinner.setSelection(it.getInt(MEDIA_LIST_SORT_KEY))
            } ?: let {
                getDiscoverMediaListSort(requireContext(), type)?.let {
                    mediaListSortSpinner.setSelection(it + 1)
                }
            }
        }
    }

    override fun onPositiveClicked(dialogInterface: DialogInterface, which: Int) {
        if (dialogInterface is DynamicDialog) {
            with(dialogInterface) {
                val mediaListSort =
                    (mediaListSortSpinner.selectedItemPosition - 1).takeIf { it >= 0 }
                setDiscoverMediaListSort(
                    requireContext(),
                    arguments?.getInt(MEDIA_LIST_FILTER_TYPE)!!,
                    mediaListSort
                )
                onDoneListener?.invoke()
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(
            MEDIA_LIST_SORT_KEY,
            dialog?.mediaListSortSpinner!!.selectedItemPosition
        )
    }
}