package com.revolgenx.anilib.review.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.review.data.field.RateReviewField
import com.revolgenx.anilib.review.data.field.ReviewField
import com.revolgenx.anilib.review.data.field.ReviewListField
import com.revolgenx.anilib.review.data.service.ReviewService
import com.revolgenx.anilib.review.data.source.ReviewListPagingSource
import com.revolgenx.anilib.review.ui.model.ReviewModel
import com.revolgenx.anilib.type.ReviewRating
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

class ReviewListViewModel(
    private val service: ReviewService
) : PagingViewModel<ReviewModel, ReviewListField, ReviewListPagingSource>() {

    override var field: ReviewListField = ReviewListField()
    override val pagingSource: ReviewListPagingSource
        get() = ReviewListPagingSource(this.field, service)
}


class ReviewViewModel(private val reviewService: ReviewService): ResourceViewModel<ReviewModel, ReviewField>(){
    override val field: ReviewField = ReviewField()
    val showToggleErrorMsg = mutableStateOf(false)

    override fun load(): Flow<ReviewModel?> {
        return reviewService.getReview(field)
    }

    fun likeReview(reviewModel: ReviewModel) {
        val newRating = when (reviewModel.userRating.value) {
            ReviewRating.UP_VOTE -> ReviewRating.NO_VOTE
            else -> ReviewRating.UP_VOTE
        }
        rateReview(reviewModel, newRating)
    }

    fun dislikeReview(reviewModel: ReviewModel) {
        val newRating = when (reviewModel.userRating.value) {
            ReviewRating.DOWN_VOTE -> ReviewRating.NO_VOTE
            else -> ReviewRating.DOWN_VOTE
        }
        rateReview(reviewModel, newRating)
    }

    private fun rateReview(
        reviewModel: ReviewModel,
        newRating: ReviewRating
    ) {
        val ratingAmount = reviewModel.ratingAmount
        val oldUserRating = reviewModel.userRating.value
        reviewModel.userRating.value = newRating

        launch {
            reviewService.rateReview(
                RateReviewField(
                    reviewId = reviewModel.id,
                    userRating = newRating
                )
            ).onEach {
                it ?: return@onEach
                reviewModel.rating.intValue = it.rating
                ratingAmount.intValue = it.ratingAmount
            }.catch {
                reviewModel.userRating.value = oldUserRating
                showToggleErrorMsg.value = true
            }.collect()
        }
    }
}