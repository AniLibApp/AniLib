package com.revolgenx.anilib.ui.viewmodel.review

import androidx.lifecycle.MediatorLiveData
import com.revolgenx.anilib.data.field.review.RateReviewField
import com.revolgenx.anilib.data.field.review.ReviewField
import com.revolgenx.anilib.data.model.review.ReviewModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.service.review.ReviewService
import com.revolgenx.anilib.ui.viewmodel.BaseViewModel

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
