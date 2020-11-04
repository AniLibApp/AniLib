package com.revolgenx.anilib.data.field.media

import com.revolgenx.anilib.MediaRecommendationQuery
import com.revolgenx.anilib.data.field.BaseSourceField
import com.revolgenx.anilib.type.RecommendationSort

class MediaRecommendationField : BaseSourceField<MediaRecommendationQuery>() {
    var mediaId: Int? = null
    var sort: Int? = null

    override fun toQueryOrMutation(): MediaRecommendationQuery {
        return MediaRecommendationQuery.builder()
            .mediaId(mediaId)
            .page(page)
            .perPage(perPage).apply {
                sort?.let {
                    sort(listOf(RecommendationSort.values()[it]))
                }
            }
            .build()
    }

}