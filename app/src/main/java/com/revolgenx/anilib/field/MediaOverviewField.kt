package com.revolgenx.anilib.field

import com.revolgenx.anilib.MediaOverViewQuery

class MediaOverviewField : BaseField<MediaOverViewQuery> {
    var mediaId: Int = -1
    override fun toQueryOrMutation(): MediaOverViewQuery {
        return MediaOverViewQuery.builder()
            .mediaId(mediaId)
            .build()
    }

}
