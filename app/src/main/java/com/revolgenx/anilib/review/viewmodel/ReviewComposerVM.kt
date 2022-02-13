package com.revolgenx.anilib.review.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.common.preference.UserPreference
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.review.service.ReviewService
import com.revolgenx.anilib.review.data.field.ReviewField
import com.revolgenx.anilib.review.data.field.SaveReviewField
import com.revolgenx.anilib.review.data.model.ReviewModel
import io.reactivex.disposables.CompositeDisposable

class ReviewComposerVM(private val reviewService: ReviewService) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    val field = ReviewField()
    val saveField = SaveReviewField()
    val reviewLiveData = MutableLiveData<Resource<ReviewModel?>>()
    val deleteLiveData = MutableLiveData<Resource<Boolean>>()
    val saveReviewLiveData = MutableLiveData<Resource<Int>>()

    val review get() = reviewLiveData.value?.data
    val showDelete get() = review?.let { it.userId == UserPreference.userId } ?: false

//    val showEdit get() = review?.let { it.userId == UserPreference.userId } ?: false

    fun getReview() {
        reviewLiveData.value = Resource.loading(null)
        reviewService.getReview(field, compositeDisposable) {
            it.data?.toSaveField()
            reviewLiveData.value = it
        }
    }

    private fun ReviewModel.toSaveField() {
        saveField.id = id
        saveField.mediaId = mediaId
        saveField.summary = summary
        saveField.body = body
        saveField.private = private
        saveField.score = score
    }

    fun saveReview() {
        saveReviewLiveData.value = Resource.loading(null)
        reviewService.saveReview(saveField, compositeDisposable) {
            saveReviewLiveData.value = it
        }
    }

    fun deleteReview() {
        reviewService.deleteReview(saveField.id, compositeDisposable) {
            deleteLiveData.value = it
        }
    }
}