package com.revolgenx.anilib.home.recommendation.data.service

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.home.recommendation.data.field.RecommendationField
import com.revolgenx.anilib.home.recommendation.data.field.SaveRecommendationField
import com.revolgenx.anilib.home.recommendation.ui.model.RecommendationModel
import com.revolgenx.anilib.home.recommendation.ui.model.SaveRecommendationModel
import kotlinx.coroutines.flow.Flow

interface RecommendationService {
    fun getRecommendationList(field: RecommendationField): Flow<PageModel<RecommendationModel>>
    fun saveRecommendation(field: SaveRecommendationField): Flow<SaveRecommendationModel?>
}
