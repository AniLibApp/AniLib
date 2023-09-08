package com.revolgenx.anilib.media.data.source

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.source.BasePagingSource
import com.revolgenx.anilib.home.recommendation.ui.model.RecommendationModel
import com.revolgenx.anilib.media.data.field.MediaRecommendationField
import com.revolgenx.anilib.media.data.service.MediaService
import kotlinx.coroutines.flow.single

class MediaRecommendationSource(
    field: MediaRecommendationField,
    private val service: MediaService
) : BasePagingSource<RecommendationModel, MediaRecommendationField>(field) {

    override suspend fun loadPage(): PageModel<RecommendationModel> {
        return service.getMediaRecommendationList(field).single()
    }

}