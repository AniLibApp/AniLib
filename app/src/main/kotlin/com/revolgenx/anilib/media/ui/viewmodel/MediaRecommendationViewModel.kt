package com.revolgenx.anilib.media.ui.viewmodel

import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.home.recommendation.ui.model.RecommendationModel
import com.revolgenx.anilib.media.data.field.MediaRecommendationField
import com.revolgenx.anilib.media.data.service.MediaService
import com.revolgenx.anilib.media.data.source.MediaRecommendationSource

class MediaRecommendationViewModel(private val mediaService: MediaService) :
    PagingViewModel<RecommendationModel, MediaRecommendationField, MediaRecommendationSource>() {
    override val field = MediaRecommendationField()
    override val pagingSource: MediaRecommendationSource
        get() = MediaRecommendationSource(this.field, mediaService)
}