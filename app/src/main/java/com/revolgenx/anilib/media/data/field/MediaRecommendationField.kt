package com.revolgenx.anilib.media.data.field

import com.revolgenx.anilib.MediaRecommendationQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField
import com.revolgenx.anilib.type.RecommendationSort

class MediaRecommendationField : BaseSourceField<MediaRecommendationQuery>() {
    var mediaId: Int? = null
    var sort: Int? = null

    override fun toQueryOrMutation(): MediaRecommendationQuery {
        val mediaSort = sort?.let {
            listOf(RecommendationSort.values()[it])
        }

        return MediaRecommendationQuery(
            page = nn(page),
            perPage = nn(perPage),
            mediaId = nn(mediaId),
            sort = nn(mediaSort),
            isAdult = nn(canShowAdult.takeIf { it.not() })
        )
    }

}