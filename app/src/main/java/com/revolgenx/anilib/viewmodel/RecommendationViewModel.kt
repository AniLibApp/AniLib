package com.revolgenx.anilib.viewmodel

import com.revolgenx.anilib.field.recommendation.RecommendationField
import com.revolgenx.anilib.service.RecommendationService
import com.revolgenx.anilib.source.RecommendationSource

class RecommendationViewModel(private val recommendationService: RecommendationService) :
    SourceViewModel<RecommendationSource, RecommendationField>() {
    override fun createSource(field: RecommendationField): RecommendationSource {
        source = RecommendationSource(field, recommendationService, compositeDisposable)
        return source!!
    }
}
