package com.revolgenx.anilib.home.recommendation.ui.viewmodel

import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.home.recommendation.data.field.RecommendationField
import com.revolgenx.anilib.home.recommendation.data.service.RecommendationService
import com.revolgenx.anilib.home.recommendation.data.source.RecommendationSource
import com.revolgenx.anilib.home.recommendation.ui.model.RecommendationModel

class RecommendationViewModel(private val service: RecommendationService) :
    PagingViewModel<RecommendationModel, RecommendationField, RecommendationSource>() {
    override val field: RecommendationField = RecommendationField()
    override val pagingSource: RecommendationSource
        get() = RecommendationSource(this.field, service)
}