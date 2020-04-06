package com.revolgenx.anilib.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.revolgenx.anilib.field.recommendation.RecommendationField
import com.revolgenx.anilib.field.recommendation.UpdateRecommendationField
import com.revolgenx.anilib.model.UpdateRecommendationModel
import com.revolgenx.anilib.model.recommendation.RecommendationModel
import com.revolgenx.anilib.repository.util.Resource
import com.revolgenx.anilib.service.RecommendationService
import com.revolgenx.anilib.source.RecommendationSource

class RecommendationViewModel(private val recommendationService: RecommendationService) :
    SourceViewModel<RecommendationSource, RecommendationField>() {

    val recommendedList by lazy {
        mutableMapOf<Int, RecommendationModel>()
    }

    override fun createSource(field: RecommendationField): RecommendationSource {
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
