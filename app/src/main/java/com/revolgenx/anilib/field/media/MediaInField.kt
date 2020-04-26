package com.revolgenx.anilib.field.media

import com.revolgenx.anilib.MediaInQuery
import com.revolgenx.anilib.field.BaseField

class MediaInField : BaseField<MediaInQuery> {
    var mediaIds: List<Int>? = null
    override fun toQueryOrMutation(): MediaInQuery {
        return MediaInQuery.builder()
            .idIn(mediaIds)
            .build()
    }
}
