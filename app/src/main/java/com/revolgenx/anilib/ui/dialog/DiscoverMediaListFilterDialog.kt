package com.revolgenx.anilib.ui.dialog

import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.pranavpandey.android.dynamic.support.model.DynamicSpinnerItem
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.getApplicationLocale
import com.revolgenx.anilib.common.ui.dialog.BaseDialogFragment
import com.revolgenx.anilib.common.preference.getDiscoverMediaListSort
import com.revolgenx.anilib.common.preference.setDiscoverMediaListSort
import com.revolgenx.anilib.databinding.MediaListFilterDialogLayoutBinding
import com.revolgenx.anilib.ui.view.makeSpinnerAdapter

class DiscoverMediaListFilterDialog : BaseDialogFragment<MediaListFilterDialogLayoutBinding>() {
    override var positiveText: Int? = R.string.done
    override var negativeText: Int? = R.string.cancel
    override var titleRes: Int? = R.string.filter


    var onDoneListener: (() -> Unit)? = null

    private val mediaListSortStrings by lazy {
        requireContext().resources.getStringArray(R.array.media_list_sort)
    }

    override fun bindView(): MediaListFilterDialogLayoutBinding {
        return MediaListFilterDialogLayoutBinding.inflate(provideLayoutInflater)
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
        with(binding) {
            val type = arguments?.getInt(MEDIA_LIST_FILTER_TYPE)!!
            val mediaListSortItems = mutableListOf<DynamicSpinnerItem>().apply {
                add(DynamicSpinnerItem(null, getString(R.string.none)))
                val canShowSpinnerIcon = getApplicationLocale() == "de"
                addAll(mediaListSortStrings.mapIndexed { index, s ->
                    var icon: Drawable? = null
                    if(canShowSpinnerIcon) {
                        icon = if (index % 2 == 0) {
                            ContextCompat.getDrawable(requireContext(), R.drawable.ic_asc)
                        } else {
                            ContextCompat.getDrawable(requireContext(), R.drawable.ic_desc)
                        }
                    }
                    DynamicSpinnerItem(icon, s)
                })
            }
            mediaListSortSpinner.adapter = makeSpinnerAdapter(requireContext(), mediaListSortItems)
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
            val mediaListSort =
                (binding.mediaListSortSpinner.selectedItemPosition - 1).takeIf { it >= 0 }
            setDiscoverMediaListSort(
                requireContext(),
                arguments?.getInt(MEDIA_LIST_FILTER_TYPE)!!,
                mediaListSort
            )
            onDoneListener?.invoke()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(
            MEDIA_LIST_SORT_KEY,
            binding.mediaListSortSpinner.selectedItemPosition
        )
    }
}