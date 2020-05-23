package com.revolgenx.anilib.service.review

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.field.review.AllReviewField
import com.revolgenx.anilib.field.review.RateReviewField
import com.revolgenx.anilib.field.review.ReviewField
import com.revolgenx.anilib.model.review.ReviewModel
import com.revolgenx.anilib.repository.util.Resource
import io.reactivex.disposables.CompositeDisposable

interface ReviewService {
    abstract val reviewLiveData: MutableLiveData<Resource<ReviewModel>>

    fun getReview(
        field: ReviewField,
        compositeDisposable: CompositeDisposable
    )

    fun getAllReview(
        field: AllReviewField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<List<ReviewModel>>) -> Unit
    )

    fun saveReview(
        field: ReviewField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<Boolean>) -> Unit
    )

    fun deleteReview(
        field: ReviewField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<Boolean>) -> Unit
    )

    fun rateReview(
        field: RateReviewField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<ReviewModel>) -> Unit
    )
}