package com.revolgenx.anilib.media.data.field

import com.revolgenx.anilib.MediaRecommendationQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField

class MediaRecommendationField :
    BaseSourceField<MediaRecommendationQuery>() {
    var mediaId: Int = -1
    override fun toQueryOrMutation(): MediaRecommendationQuery {
        return MediaRecommendationQuery(
            page = nn(page),
            perPage = nn(perPage),
            mediaId = nn(mediaId),
            isAdult = nn(canShowAdult.takeIf { it.not() })
        )
    }
}
