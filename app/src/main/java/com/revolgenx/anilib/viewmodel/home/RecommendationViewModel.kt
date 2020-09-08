package com.revolgenx.anilib.viewmodel.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.revolgenx.anilib.field.recommendation.RecommendationField
import com.revolgenx.anilib.field.recommendation.UpdateRecommendationField
import com.revolgenx.anilib.model.UpdateRecommendationModel
import com.revolgenx.anilib.model.recommendation.RecommendationModel
import com.revolgenx.anilib.repository.util.Resource
import com.revolgenx.anilib.service.recommendation.RecommendationService
import com.revolgenx.anilib.source.RecommendationSource
import com.revolgenx.anilib.viewmodel.SourceViewModel

class RecommendationViewModel(private val recommendationService: RecommendationService) :
    SourceViewModel<RecommendationSource, RecommendationField>() {

    val recommendedList by lazy {
        mutableMapOf<Int, RecommendationModel>()
    }

    override var field: RecommendationField=RecommendationField()

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
