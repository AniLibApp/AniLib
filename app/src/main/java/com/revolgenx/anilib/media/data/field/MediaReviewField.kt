package com.revolgenx.anilib.media.data.field

import com.revolgenx.anilib.MediaReviewQuery
import com.revolgenx.anilib.common.data.field.BaseField

class MediaReviewField : BaseField<MediaReviewQuery>(){
    var mediaId = -1
    var page = 1
    var perPage = PER_PAGE

    override fun toQueryOrMutation(): MediaReviewQuery {
        return MediaReviewQuery(page = nn(page), perPage = nn(perPage), mediaId = nn(mediaId))
    }
}