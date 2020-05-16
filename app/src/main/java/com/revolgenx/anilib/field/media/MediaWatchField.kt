package com.revolgenx.anilib.field.media

import com.revolgenx.anilib.MediaWatchQuery
import com.revolgenx.anilib.field.BaseField

class MediaWatchField :
    BaseField<MediaWatchQuery>() {
    var mediaId = -1
    override fun toQueryOrMutation(): MediaWatchQuery {
        return MediaWatchQuery.builder()
            .mediaId(mediaId)
            .build()
    }
}