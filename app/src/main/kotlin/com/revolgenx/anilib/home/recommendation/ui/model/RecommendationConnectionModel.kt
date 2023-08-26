package com.revolgenx.anilib.home.recommendation.ui.model

import com.revolgenx.anilib.fragment.PageInfo

data class RecommendationConnectionModel(
    val pageInfo: PageInfo?,
    val nodes: List<RecommendationModel>?
)
