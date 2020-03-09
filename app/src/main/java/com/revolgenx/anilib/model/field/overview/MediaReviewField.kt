package com.revolgenx.anilib.model.field.overview

import com.revolgenx.anilib.MediaReviewQuery
import com.revolgenx.anilib.model.field.BaseField
import com.revolgenx.anilib.model.field.BaseField.Companion.PER_PAGE

class MediaReviewField :BaseField<MediaReviewQuery>{
    var mediaId = -1
    var page = 1
    var perPage = PER_PAGE

    override fun toQuery(): MediaReviewQuery {
        return MediaReviewQuery.builder()
            .page(page)
            .perPage(perPage)
            .mediaId(mediaId)
            .build()
    }

}