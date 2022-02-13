package com.revolgenx.anilib.review.service

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.review.data.field.AllReviewField
import com.revolgenx.anilib.review.data.field.RateReviewField
import com.revolgenx.anilib.review.data.field.ReviewField
import com.revolgenx.anilib.review.data.field.SaveReviewField
import com.revolgenx.anilib.review.data.model.ReviewModel
import io.reactivex.disposables.CompositeDisposable

interface ReviewService {
    fun getReview(
        field: ReviewField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<ReviewModel?>) -> Unit
    )

    fun getAllReview(
        field: AllReviewField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<List<ReviewModel>>) -> Unit
    )

    fun saveReview(
        field: SaveReviewField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<Int>) -> Unit
    )

    fun deleteReview(
        id:Int?,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<Boolean>) -> Unit
    )

    fun rateReview(
        field: RateReviewField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<ReviewModel>) -> Unit
    )
}