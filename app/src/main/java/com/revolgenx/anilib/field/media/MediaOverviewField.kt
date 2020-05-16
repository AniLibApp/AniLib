package com.revolgenx.anilib.field.media

import com.revolgenx.anilib.MediaOverViewQuery
import com.revolgenx.anilib.field.BaseField

class MediaOverviewField :
    BaseField<MediaOverViewQuery>() {
    var mediaId: Int? = null
    override fun toQueryOrMutation(): MediaOverViewQuery {
        return MediaOverViewQuery.builder()
            .mediaId(mediaId)
            .build()
    }

}
