package com.revolgenx.anilib.model.field.overview

import com.revolgenx.anilib.MediaWatchQuery
import com.revolgenx.anilib.model.field.BaseField

class MediaWatchField :
    BaseField<MediaWatchQuery> {
    var mediaId = -1
    override fun toQuery(): MediaWatchQuery {
        return MediaWatchQuery.builder()
            .mediaId(mediaId)
            .build()
    }
}