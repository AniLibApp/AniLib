package com.revolgenx.anilib.home.recommendation.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.home.recommendation.data.field.RecommendationField
import com.revolgenx.anilib.home.recommendation.data.field.SaveRecommendationField
import com.revolgenx.anilib.home.recommendation.data.service.RecommendationService
import com.revolgenx.anilib.home.recommendation.data.source.RecommendationPagingSource
import com.revolgenx.anilib.home.recommendation.ui.model.RecommendationModel
import com.revolgenx.anilib.type.RecommendationRating
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

class RecommendationViewModel(private val service: RecommendationService) :
    PagingViewModel<RecommendationModel, RecommendationField, RecommendationPagingSource>() {
    override var field: RecommendationField = RecommendationField()
    override val pagingSource: RecommendationPagingSource
        get() = RecommendationPagingSource(this.field, service)

    val showToggleErrorMsg = mutableStateOf(false)

    fun likeRecommendation(recommendationModel: RecommendationModel) {
        val newRating = when (recommendationModel.userRating.value) {
            RecommendationRating.RATE_UP -> RecommendationRating.NO_RATING
            else -> RecommendationRating.RATE_UP
        }
        saveRecommendation(recommendationModel, newRating)
    }

    fun dislikeRecommendation(recommendationModel: RecommendationModel) {
        val newRating = when (recommendationModel.userRating.value) {
            RecommendationRating.RATE_DOWN -> RecommendationRating.NO_RATING
            else -> RecommendationRating.RATE_DOWN
        }
        saveRecommendation(recommendationModel, newRating)
    }

    private fun saveRecommendation(
        recommendationModel: RecommendationModel,
        newRating: RecommendationRating
    ) {
        val rating = recommendationModel.rating
        val oldUserRating = recommendationModel.userRating.value
        recommendationModel.userRating.value = newRating

        launch {
            service.saveRecommendation(
                SaveRecommendationField(
                    mediaId = recommendationModel.media?.id,
                    mediaRecommendationId = recommendationModel.mediaRecommendation?.id,
                    rating = newRating
                )
            ).onEach {
                it ?: return@onEach
                rating.intValue = it.rating
            }.catch {
                recommendationModel.userRating.value = oldUserRating
                showToggleErrorMsg.value = true
            }.collect()
        }
    }
}