package com.revolgenx.anilib.fragment

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.field.reivew.ReviewField
import com.revolgenx.anilib.model.review.ReviewModel
import com.revolgenx.anilib.repository.util.Resource
import com.revolgenx.anilib.service.review.ReviewService
import io.reactivex.disposables.CompositeDisposable

class ReviewComposerViewModel(private val reviewService: ReviewService) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val field = ReviewField()

    val reviewLiveData = MediatorLiveData<Resource<ReviewModel>>().also {
        it.addSource(reviewService.reviewLiveData) { resource ->
            it.value = resource
        }
    }

    fun getReview() {
        reviewService.getReview(field, compositeDisposable)
    }
}
