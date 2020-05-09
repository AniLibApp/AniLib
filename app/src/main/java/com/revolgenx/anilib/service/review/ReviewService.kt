package com.revolgenx.anilib.service.review

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.field.reivew.ReviewField
import com.revolgenx.anilib.model.review.ReviewModel
import com.revolgenx.anilib.repository.util.Resource
import io.reactivex.disposables.CompositeDisposable

interface ReviewService {
    abstract val reviewLiveData: MutableLiveData<Resource<ReviewModel>>

    fun getReview(
        field: ReviewField,
        compositeDisposable: CompositeDisposable
    )
}