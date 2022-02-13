package com.revolgenx.anilib.review.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.bottomsheet.DynamicBottomSheetFragment
import com.revolgenx.anilib.constant.ALReviewSort
import com.revolgenx.anilib.databinding.ReviewsFilterBottomSheetBinding
import com.revolgenx.anilib.ui.dialog.sorting.AniLibSortingModel
import com.revolgenx.anilib.ui.dialog.sorting.SortOrder

class ReviewFilterBottomSheet : DynamicBottomSheetFragment<ReviewsFilterBottomSheetBinding>() {
    companion object {
        fun newInstance(sort: Int): ReviewFilterBottomSheet {
            return ReviewFilterBottomSheet().also {
                it.arguments = bundleOf(reviews_filter_key to sort)
            }
        }

        private const val reviews_filter_key = "reviews_filter_key"
    }

    private val reviewsFilterSortItems by lazy {
        requireContext().resources.getStringArray(R.array.al_review_sort)
    }

    var positiveCallback: ((Int?) -> Unit)? = null

    override fun bindView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ReviewsFilterBottomSheetBinding {
        return ReviewsFilterBottomSheetBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var reviewSortOrder = SortOrder.NONE
        var savedReviewSortIndex = -1
        val reviewSortEnums = ALReviewSort.values()

        (arguments?.getInt(reviews_filter_key) ?: 0).let { savedSort ->
            reviewSortOrder = if (savedSort % 2 == 0) {
                savedReviewSortIndex = reviewSortEnums.first { it.sort == savedSort }.ordinal
                SortOrder.ASC
            } else {
                savedReviewSortIndex = reviewSortEnums.first { it.sort == savedSort - 1 }.ordinal
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
        onPositiveClicked = {
            positiveCallback?.invoke(arguments?.getInt(reviews_filter_key))
        }
    }

}