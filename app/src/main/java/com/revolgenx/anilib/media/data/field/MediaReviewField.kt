package com.revolgenx.anilib.media.data.field

import com.revolgenx.anilib.MediaReviewQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField

class MediaReviewField : BaseSourceField<MediaReviewQuery>(){
    var mediaId = -1

    override fun toQueryOrMutation(): MediaReviewQuery {
        return MediaReviewQuery(page = nn(page), perPage = nn(perPage), mediaId = nn(mediaId))
    }
}