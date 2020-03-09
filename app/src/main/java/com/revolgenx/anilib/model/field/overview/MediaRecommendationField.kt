package com.revolgenx.anilib.model.field.overview

import com.revolgenx.anilib.MediaRecommendationQuery
import com.revolgenx.anilib.model.field.BaseField
import com.revolgenx.anilib.type.RecommendationSort

class MediaRecommendationField :
    BaseField<MediaRecommendationQuery> {
    var mediaId: Int = -1
    var sort = RecommendationSort.RATING_DESC.ordinal
    var page: Int = 0
    var perPage = 10

    override fun toQuery(): MediaRecommendationQuery {
        return MediaRecommendationQuery.builder()
            .page(page)
            .perPage(perPage)
            .sort(listOf(RecommendationSort.values()[sort]))
            .build()
    }

}