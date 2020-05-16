package com.revolgenx.anilib.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.field.reivew.ReviewField
import com.revolgenx.anilib.model.review.ReviewModel
import com.revolgenx.anilib.repository.util.Resource
import com.revolgenx.anilib.service.review.ReviewService
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
