package com.revolgenx.anilib.dialog

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import com.pranavpandey.android.dynamic.support.adapter.DynamicSpinnerImageAdapter
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicDialogFragment
import com.pranavpandey.android.dynamic.support.model.DynamicSpinnerItem
import com.pranavpandey.android.dynamic.support.widget.DynamicButton
import com.revolgenx.anilib.R
import com.revolgenx.anilib.util.onItemSelected
import kotlinx.android.synthetic.main.reviews_filter_dialog_layout.*

class ReviewsFilterDialog : BaseDialogFragment() {


    companion object {
        fun newInstance(sort: Int): ReviewsFilterDialog {
            return ReviewsFilterDialog().also {
                it.arguments = bundleOf(reviews_filter_key to sort)
            }
        }

        private const val reviews_filter_key = "reviews_filter_key"
    }

    private val reviewsFilterSpinnerItem by lazy {
        requireContext().resources.getStringArray(R.array.review_sort).map {
            DynamicSpinnerItem(
                null, it
            )
        }
    }

    var positiveCallback: ((Int?) -> Unit)? = null

    override var titleRes: Int? = R.string.filter
    override var viewRes: Int? = R.layout.reviews_filter_dialog_layout
    override var positiveText: Int? = R.string.done
    override var negativeText: Int? = R.string.cancel

    override fun onPositiveClicked(dialogInterface: DialogInterface, which: Int) {
        super.onPositiveClicked(dialogInterface, which)
        if (dialogInterface is DynamicDialog) {
            positiveCallback?.invoke(arguments?.getInt(reviews_filter_key))
        }
    }

    override fun onShowListener(alertDialog: DynamicDialog, savedInstanceState: Bundle?) {
        with(alertDialog){
            reviewsFilterSpinner.adapter = makeSpinnerAdapter(reviewsFilterSpinnerItem)
            reviewsFilterSpinner.setSelection(arguments?.getInt(reviews_filter_key) ?: 0)
            reviewsFilterSpinner.onItemSelected {
                arguments = bundleOf(reviews_filter_key to it)
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