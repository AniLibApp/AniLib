package com.revolgenx.anilib.home.recommendation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.revolgenx.anilib.home.recommendation.data.field.RecommendationField
import com.revolgenx.anilib.home.recommendation.data.field.UpdateRecommendationField
import com.revolgenx.anilib.home.recommendation.data.model.RecommendationModel
import com.revolgenx.anilib.home.recommendation.data.model.UpdateRecommendationModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.home.recommendation.service.RecommendationService
import com.revolgenx.anilib.infrastructure.source.RecommendationSource
import com.revolgenx.anilib.common.viewmodel.SourceViewModel

class RecommendationViewModel(private val recommendationService: RecommendationService) :
    SourceViewModel<RecommendationSource, RecommendationField>() {

    val recommendedList by lazy {
        mutableMapOf<Int, RecommendationModel>()
    }

    override var field: RecommendationField = RecommendationField()

    override fun createSource(): RecommendationSource {
        source = RecommendationSource(field, recommendationService, compositeDisposable)
        return source!!
    }

    fun updateRecommendation(field: UpdateRecommendationField): MutableLiveData<Resource<UpdateRecommendationModel>> {
        return recommendationService.updateRecommendation(field, compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        recommendedList.clear()
    }

    fun removeUpdateRecommendationObserver(observer: Observer<Resource<UpdateRecommendationModel>>) {
        recommendationService.removeUpdateRecommendationObserver(observer)
    }
}