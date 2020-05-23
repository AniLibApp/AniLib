package com.revolgenx.anilib.viewmodel

import androidx.lifecycle.MediatorLiveData
import com.revolgenx.anilib.field.review.RateReviewField
import com.revolgenx.anilib.field.review.ReviewField
import com.revolgenx.anilib.model.review.ReviewModel
import com.revolgenx.anilib.repository.util.Resource
import com.revolgenx.anilib.service.review.ReviewService

class ReviewViewModel(private val reviewService: ReviewService) : BaseViewModel() {
    val field = ReviewField()
    val reviewLiveData = MediatorLiveData<Resource<ReviewModel>>().also {
        it.addSource(reviewService.reviewLiveData) { res ->
            it.value = res
        }
    }

    fun getReview() {
        reviewLiveData.value = Resource.loading(null)
        reviewService.getReview(field, compositeDisposable)
    }

    fun rateReview(rateField: RateReviewField, callback: (Resource<ReviewModel>) -> Unit) {
        reviewService.rateReview(rateField, compositeDisposable) {
            callback.invoke(it)
        }
    }
}
