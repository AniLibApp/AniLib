package com.revolgenx.anilib.home.recommendation.data.source

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.source.BasePagingSource
import com.revolgenx.anilib.home.recommendation.data.field.RecommendationField
import com.revolgenx.anilib.home.recommendation.data.service.RecommendationService
import com.revolgenx.anilib.home.recommendation.ui.model.RecommendationModel
import kotlinx.coroutines.flow.single

class RecommendationSource(field: RecommendationField, private val service: RecommendationService) :
    BasePagingSource<RecommendationModel, RecommendationField>(field) {
    override suspend fun loadPage(): PageModel<RecommendationModel> {
        return service.getRecommendationList(field).single()
    }
}
