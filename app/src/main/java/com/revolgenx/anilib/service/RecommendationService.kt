package com.revolgenx.anilib.service

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.model.SaveRecommendationModel
import com.revolgenx.anilib.field.AddRecommendationField
import com.revolgenx.anilib.model.MediaRecommendationModel
import com.revolgenx.anilib.field.UpdateRecommendationField
import com.revolgenx.anilib.field.overview.MediaRecommendationField
import com.revolgenx.anilib.model.UpdateRecommendationModel
import com.revolgenx.anilib.repository.network.BaseGraphRepository
import com.revolgenx.anilib.repository.util.Resource
import io.reactivex.disposables.CompositeDisposable

abstract class RecommendationService(protected var graphRepository: BaseGraphRepository) {
    val mediaRecommendationLiveData
            by lazy { MutableLiveData<Resource<List<MediaRecommendationModel>>>() }

    val updateRecommendationLiveData by lazy {
        MutableLiveData<Resource<UpdateRecommendationModel>>()
    }

    val saveRecommendationModel by lazy {
        MutableLiveData<Resource<SaveRecommendationModel>>()
    }

    abstract fun mediaRecommendation(
        field: MediaRecommendationField,
        compositeDisposable: CompositeDisposable? = null
    ): MutableLiveData<Resource<List<MediaRecommendationModel>>>


    abstract fun updateRecommendation(
        recommendationField: UpdateRecommendationField,
        compositeDisposable: CompositeDisposable? = null
    ): MutableLiveData<Resource<UpdateRecommendationModel>>

    abstract fun saveRecommendation(addRecommendationField: AddRecommendationField):MutableLiveData<Resource<SaveRecommendationModel>>
}