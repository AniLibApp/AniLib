package com.revolgenx.anilib.infrastructure.service.recommendation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.revolgenx.anilib.data.field.media.MediaRecommendationField
import com.revolgenx.anilib.data.field.recommendation.AddRecommendationField
import com.revolgenx.anilib.data.field.recommendation.RecommendationField
import com.revolgenx.anilib.data.field.recommendation.UpdateRecommendationField
import com.revolgenx.anilib.data.model.media_info.MediaRecommendationModel
import com.revolgenx.anilib.data.model.recommendation.SaveRecommendationModel
import com.revolgenx.anilib.data.model.recommendation.UpdateRecommendationModel
import com.revolgenx.anilib.data.model.recommendation.RecommendationModel
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.util.Resource
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