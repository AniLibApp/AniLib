package com.revolgenx.anilib.review.viewmodel

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.common.preference.UserPreference
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.review.service.ReviewService
import com.revolgenx.anilib.review.data.field.RateReviewField
import com.revolgenx.anilib.review.data.field.ReviewField
import com.revolgenx.anilib.review.data.model.ReviewModel
import com.revolgenx.anilib.common.viewmodel.BaseViewModel
import com.revolgenx.anilib.common.repository.util.Status
import com.revolgenx.anilib.type.ReviewRating

class ReviewVM(private val reviewService: ReviewService) : BaseViewModel() {
    val field = ReviewField()
    val rateReviewField = RateReviewField()
    val reviewLiveData = MutableLiveData<Resource<ReviewModel?>>()
    val rateReviewLiveData = MutableLiveData<Resource<ReviewModel>>()

    val review get() = reviewLiveData.value?.data
    val showEdit get() = review?.let { it.userId == UserPreference.userId } ?: false

    fun getReview() {
        reviewLiveData.value = Resource.loading(null)
        reviewService.getReview(field, compositeDisposable) {
            it.data?.let {
                rateReviewField.reviewId = it.id
                rateReviewField.userRating = it.userRating
            }
            reviewLiveData.value = it
        }
    }

    fun upVoteReview() {
        review ?: return
        rateReviewField.userRating =
            when (rateReviewField.userRating) {
                ReviewRating.NO_VOTE.ordinal, ReviewRating.DOWN_VOTE.ordinal -> {
                    ReviewRating.UP_VOTE.ordinal
                }
                else -> {
                    ReviewRating.NO_VOTE.ordinal
                }
            }
        rateReview()
    }

    fun downVoteReview() {
        review ?: return
        rateReviewField.userRating =
            when (rateReviewField.userRating) {
                ReviewRating.NO_VOTE.ordinal, ReviewRating.UP_VOTE.ordinal -> {
                    ReviewRating.DOWN_VOTE.ordinal
                }
                else -> {
                    ReviewRating.NO_VOTE.ordinal
                }
            }
        rateReview()
    }

    private fun rateReview() {
        rateReviewLiveData.value = Resource.success(review)
        reviewService.rateReview(rateReviewField, compositeDisposable) rateReviewS@{
            when (it.status) {
                Status.ERROR -> {
                    rateReviewField.userRating = review?.userRating
                    rateReviewLiveData.value = it
                }
                Status.SUCCESS -> {
                    val review = review ?: return@rateReviewS
                    val rateReview = it.data ?: return@rateReviewS
                    with(review) {
                        userRating = rateReview.userRating
                        rating = rateReview.rating
                        ratingAmount = rateReview.ratingAmount
                    }
                    rateReviewLiveData.value = it
                }
                Status.LOADING -> {}
            }
        }
    }
}