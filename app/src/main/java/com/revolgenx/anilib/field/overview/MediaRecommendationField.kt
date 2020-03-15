package com.revolgenx.anilib.field.overview

import com.revolgenx.anilib.MediaRecommendationQuery
import com.revolgenx.anilib.field.BaseField
import com.revolgenx.anilib.type.RecommendationSort

class MediaRecommendationField :
    BaseField<MediaRecommendationQuery> {
    var mediaId: Int = -1
    var sort = RecommendationSort.RATING_DESC.ordinal
    var page: Int = 0
    var perPage = 10

    override fun toQueryOrMutation(): MediaRecommendationQuery {
        return MediaRecommendationQuery.builder()
            .mediaId(mediaId)
            .page(page)
            .perPage(perPage)
            .sort(listOf(RecommendationSort.values()[sort]))
            .build()
    }

}