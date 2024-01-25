package com.revolgenx.anilib.home.recommendation.ui.model

import com.revolgenx.anilib.type.RecommendationRating

data class SaveRecommendationModel(
    val id: Int,
    val rating: Int,
    val userRating: RecommendationRating
)