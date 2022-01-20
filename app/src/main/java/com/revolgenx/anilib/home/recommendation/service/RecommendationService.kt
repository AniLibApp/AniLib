package com.revolgenx.anilib.home.recommendation.service

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.revolgenx.anilib.home.recommendation.data.field.AddRecommendationField
import com.revolgenx.anilib.home.recommendation.data.field.RecommendationField
import com.revolgenx.anilib.home.recommendation.data.field.UpdateRecommendationField
import com.revolgenx.anilib.home.recommendation.data.model.RecommendationModel
import com.revolgenx.anilib.home.recommendation.data.model.SaveRecommendationModel
import com.revolgenx.anilib.home.recommendation.data.model.UpdateRecommendationModel
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.media.data.field.MediaRecommendationField
import com.revolgenx.anilib.media.data.model.MediaRecommendationModel
import io.reactivex.disposables.CompositeDisposable

abstract class RecommendationService(protected var graphRepository: BaseGraphRepository) {

    val updateRecommendationLiveData by lazy {
        MutableLiveData<Resource<UpdateRecommendationModel>>()
    }

    val saveRecommendationModel by lazy {
        MutableLiveData<Resource<SaveRecommendationModel>>()
    }

    abstract fun recommendation(
        field: RecommendationField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<List<RecommendationModel>>) -> Unit
    )

    abstract fun mediaRecommendation(
        field: MediaRecommendationField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: ((Resource<List<MediaRecommendationModel>>) -> Unit)
    )


    abstract fun updateRecommendation(
        recommendationField: UpdateRecommendationField,
        compositeDisposable: CompositeDisposable? = null
    ): MutableLiveData<Resource<UpdateRecommendationModel>>

    abstract fun removeUpdateRecommendationObserver(observer: Observer<Resource<UpdateRecommendationModel>>)


    abstract fun saveRecommendation(addRecommendationField: AddRecommendationField): MutableLiveData<Resource<SaveRecommendationModel>>
}