package com.revolgenx.anilib.review.viewmodel

import androidx.lifecycle.MediatorLiveData
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.review.service.ReviewService
import com.revolgenx.anilib.review.data.field.RateReviewField
import com.revolgenx.anilib.review.data.field.ReviewField
import com.revolgenx.anilib.review.data.model.ReviewModel
import com.revolgenx.anilib.common.viewmodel.BaseViewModel

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