package com.revolgenx.anilib.field

import com.revolgenx.anilib.MediaOverViewQuery

class MediaOverviewField : BaseField<MediaOverViewQuery> {
    var mediaId: Int? = null
    override fun toQueryOrMutation(): MediaOverViewQuery {
        return MediaOverViewQuery.builder()
            .mediaId(mediaId)
            .build()
    }

}
