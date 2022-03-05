package com.revolgenx.anilib.home.recommendation.service

import com.revolgenx.anilib.home.recommendation.data.field.RecommendationField
import com.revolgenx.anilib.home.recommendation.data.field.SaveRecommendationField
import com.revolgenx.anilib.home.recommendation.data.model.RecommendationModel
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.media.data.field.MediaRecommendationField
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.Flow

interface RecommendationService {
    fun getRecommendations(
        field: RecommendationField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<List<RecommendationModel>>) -> Unit
    )

    fun getMediaRecommendations(
        field: MediaRecommendationField,
        compositeDisposable: CompositeDisposable,
        callback: ((Resource<List<RecommendationModel>>) -> Unit)
    )

    fun saveRecommendation(
        recommendationField: SaveRecommendationField
    ): Flow<Resource<RecommendationModel>>
}