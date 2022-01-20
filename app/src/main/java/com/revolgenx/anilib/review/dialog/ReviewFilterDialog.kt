package com.revolgenx.anilib.review.dialog

import android.content.DialogInterface
import android.os.Bundle
import androidx.core.os.bundleOf
import com.pranavpandey.android.dynamic.support.adapter.DynamicSpinnerImageAdapter
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.pranavpandey.android.dynamic.support.model.DynamicMenu
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.dialog.BaseDialogFragment
import com.revolgenx.anilib.constant.ALReviewSort
import com.revolgenx.anilib.databinding.ReviewsFilterDialogLayoutBinding
import com.revolgenx.anilib.ui.dialog.sorting.AniLibSortingModel
import com.revolgenx.anilib.ui.dialog.sorting.SortOrder

class ReviewFilterDialog : BaseDialogFragment<ReviewsFilterDialogLayoutBinding>() {


    companion object {
        fun newInstance(sort: Int): ReviewFilterDialog {
            return ReviewFilterDialog().also {
                it.arguments = bundleOf(reviews_filter_key to sort)
            }
        }

        private const val reviews_filter_key = "reviews_filter_key"
    }

    private val reviewsFilterSortItems by lazy {
        requireContext().resources.getStringArray(R.array.al_review_sort)
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
        var reviewSortOrder = SortOrder.NONE
        var savedReviewSortIndex = -1
        val reviewSortEnums = ALReviewSort.values()

        (arguments?.getInt(reviews_filter_key) ?: 0).let {savedSort->
            reviewSortOrder = if(savedSort % 2 == 0){
                savedReviewSortIndex = reviewSortEnums.first { it.sort == savedSort}.ordinal
                SortOrder.ASC
            }else{
                savedReviewSortIndex = reviewSortEnums.first { it.sort == savedSort - 1}.ordinal
                SortOrder.DESC
            }
        }

        val reviewSortModels = reviewsFilterSortItems.mapIndexed { index, s ->
            AniLibSortingModel(
                reviewSortEnums[index],
                s,
                if (savedReviewSortIndex == index) reviewSortOrder else SortOrder.NONE,
                allowNone = false
            )
        }

        binding.reviewSort.setSortItems(reviewSortModels)
        binding.reviewSort.onSortItemSelected = {
            val selectedSort = it?.let {
                when (it.order) {
                    SortOrder.ASC -> {
                        (it.data as ALReviewSort).sort
                    }
                    SortOrder.DESC -> {
                        (it.data as ALReviewSort).sort + 1
                    }
                    else -> {
                        0
                    }
                }
            }
            arguments = bundleOf(reviews_filter_key to selectedSort)
        }
    }

    private fun makeSpinnerAdapter(items: List<DynamicMenu>) =
        DynamicSpinnerImageAdapter(
            requireContext(),
            R.layout.ads_layout_spinner_item,
            R.id.ads_spinner_item_icon,
            R.id.ads_spinner_item_text, items
        )
}