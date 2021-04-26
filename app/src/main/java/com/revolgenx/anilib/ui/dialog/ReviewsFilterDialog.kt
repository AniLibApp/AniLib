package com.revolgenx.anilib.ui.dialog

import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.pranavpandey.android.dynamic.support.adapter.DynamicSpinnerImageAdapter
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.pranavpandey.android.dynamic.support.model.DynamicSpinnerItem
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.getApplicationLocale
import com.revolgenx.anilib.common.ui.dialog.BaseDialogFragment
import com.revolgenx.anilib.databinding.ReviewsFilterDialogLayoutBinding
import com.revolgenx.anilib.util.onItemSelected

class ReviewsFilterDialog : BaseDialogFragment<ReviewsFilterDialogLayoutBinding>() {


    companion object {
        fun newInstance(sort: Int): ReviewsFilterDialog {
            return ReviewsFilterDialog().also {
                it.arguments = bundleOf(reviews_filter_key to sort)
            }
        }

        private const val reviews_filter_key = "reviews_filter_key"
    }

    private val canShowSpinnerIcon by lazy { getApplicationLocale() == "de" }
    private val reviewsFilterSpinnerItem by lazy {
        requireContext().resources.getStringArray(R.array.review_sort).mapIndexed { index, s ->
            var icon: Drawable? = null
            if(canShowSpinnerIcon) {
                icon = if (index % 2 == 0) {
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_asc)
                } else {
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_desc)
                }
            }
            DynamicSpinnerItem(icon, s)
        }
    }

    var positiveCallback: ((Int?) -> Unit)? = null

    override var titleRes: Int? = R.string.filter
    override var positiveText: Int? = R.string.done
    override var negativeText: Int? = R.string.cancel

    override fun bindView(): ReviewsFilterDialogLayoutBinding {
        return ReviewsFilterDialogLayoutBinding.inflate(provideLayoutInflater)
    }

    override fun onPositiveClicked(dialogInterface: DialogInterface, which: Int) {
        super.onPositiveClicked(dialogInterface, which)
        if (dialogInterface is DynamicDialog) {
            positiveCallback?.invoke(arguments?.getInt(reviews_filter_key))
        }
    }

    override fun onShowListener(alertDialog: DynamicDialog, savedInstanceState: Bundle?) {
        binding.reviewsFilterSpinner.adapter = makeSpinnerAdapter(reviewsFilterSpinnerItem)
        binding.reviewsFilterSpinner.setSelection(arguments?.getInt(reviews_filter_key) ?: 0)
        binding.reviewsFilterSpinner.onItemSelected {
            arguments = bundleOf(reviews_filter_key to it)
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