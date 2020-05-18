package com.revolgenx.anilib.dialog

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

class ReviewsFilterDialog : DynamicDialogFragment() {


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

    override fun onCustomiseBuilder(
        dialogBuilder: DynamicDialog.Builder,
        savedInstanceState: Bundle?
    ): DynamicDialog.Builder {
        with(dialogBuilder) {
            setTitle(R.string.filter)
            setPositiveButton(R.string.done) { dialogInterface, _ ->
                if (dialogInterface is DynamicDialog) {
                    positiveCallback?.invoke(arguments?.getInt(reviews_filter_key))
                }
            }
            setNegativeButton(R.string.cancel) { _, _ ->
                dismiss()
            }
            setView(R.layout.reviews_filter_dialog_layout)
            isAutoDismiss = false
        }
        return super.onCustomiseBuilder(dialogBuilder, savedInstanceState)
    }

    override fun onCustomiseDialog(
        alertDialog: DynamicDialog,
        savedInstanceState: Bundle?
    ): DynamicDialog {
        with(alertDialog) {
            setOnShowListener {
                getButton(AlertDialog.BUTTON_POSITIVE)?.let {
                    (it as DynamicButton).isAllCaps = false
                }
                getButton(AlertDialog.BUTTON_NEGATIVE)?.let {
                    (it as DynamicButton).isAllCaps = false
                }
                reviewsFilterSpinner.adapter = makeSpinnerAdapter(reviewsFilterSpinnerItem)
                reviewsFilterSpinner.setSelection(arguments?.getInt(reviews_filter_key) ?: 0)
                reviewsFilterSpinner.onItemSelected {
                    arguments = bundleOf(reviews_filter_key to it)
                }
            }
        }
        return super.onCustomiseDialog(alertDialog, savedInstanceState)
    }


    private fun makeSpinnerAdapter(items: List<DynamicSpinnerItem>) =
        DynamicSpinnerImageAdapter(
            requireContext(),
            R.layout.ads_layout_spinner_item,
            R.id.ads_spinner_item_icon,
            R.id.ads_spinner_item_text, items
        )
}