package com.revolgenx.anilib.model.field

import com.revolgenx.anilib.MediaOverViewQuery

class MediaOverviewField : BaseField<MediaOverViewQuery> {
    var mediaId: Int = -1
    override fun toQuery(): MediaOverViewQuery {
        return MediaOverViewQuery.builder()
            .mediaId(mediaId)
            .build()
    }

}
