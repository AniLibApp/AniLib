package com.revolgenx.anilib.data.field.media

import com.revolgenx.anilib.MediaReviewQuery
import com.revolgenx.anilib.data.field.BaseField

class MediaReviewField :BaseField<MediaReviewQuery>(){
    var mediaId = -1
    var page = 1
    var perPage = PER_PAGE

    override fun toQueryOrMutation(): MediaReviewQuery {
        return MediaReviewQuery.builder()
            .page(page)
            .perPage(perPage)
            .mediaId(mediaId)
            .build()
    }
}