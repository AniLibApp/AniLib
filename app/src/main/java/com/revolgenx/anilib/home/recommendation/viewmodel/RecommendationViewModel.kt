package com.revolgenx.anilib.home.recommendation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.revolgenx.anilib.home.recommendation.data.field.RecommendationField
import com.revolgenx.anilib.home.recommendation.data.field.SaveRecommendationField
import com.revolgenx.anilib.home.recommendation.data.model.RecommendationModel
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.home.recommendation.service.RecommendationService
import com.revolgenx.anilib.infrastructure.source.RecommendationSource
import com.revolgenx.anilib.common.viewmodel.SourceViewModel
import com.revolgenx.anilib.type.RecommendationRating
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

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

    fun upVoteRecommendation(item: RecommendationModel) =
        saveRecommendation(item, RecommendationRating.RATE_UP.ordinal)

    fun downVoteRecommendation(item: RecommendationModel) =
        saveRecommendation(item, RecommendationRating.RATE_DOWN.ordinal)

    fun saveRecommendation(
        item: RecommendationModel,
        rating: Int
    ): LiveData<Resource<RecommendationModel>> {

        val saveField = SaveRecommendationField()
        saveField.mediaId = item.recommendationFrom?.id
        saveField.mediaRecommendationId = item.recommended?.id

        saveField.rating = if (item.userRating == rating) {
            RecommendationRating.NO_RATING.ordinal
        } else {
            rating
        }

        return recommendationService.saveRecommendation(saveField).onEach {
            it.data?.let {
                item.id = it.id
                item.userRating = it.userRating
                item.rating = it.rating
            }
        }.asLiveData()
    }

    override fun onCleared() {
        super.onCleared()
        recommendedList.clear()
    }

}