package com.revolgenx.anilib.review.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.review.service.ReviewService
import com.revolgenx.anilib.review.data.field.ReviewField
import com.revolgenx.anilib.review.data.model.ReviewModel
import io.reactivex.disposables.CompositeDisposable

class ReviewComposerViewModel(private val reviewService: ReviewService) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    val field = ReviewField()
    val reviewLiveData = MediatorLiveData<Resource<ReviewModel>>().also {
        it.addSource(reviewService.reviewLiveData) { resource ->
            it.value = resource
        }
    }

    fun getReview() {
        reviewLiveData.value = Resource.loading(null)
        reviewService.getReview(field, compositeDisposable)
    }

    fun saveReview(callback: ((Resource<Boolean>) -> Unit)) {
        callback.invoke(Resource.loading(false))
        reviewService.saveReview(field, compositeDisposable) {
            callback.invoke(it)
        }
    }

    fun deleteReview(callback: ((Resource<Boolean>) -> Unit)) {
        callback.invoke(Resource.loading(false))
        reviewService.deleteReview(field, compositeDisposable) {
            callback.invoke(it)
        }
    }
}