package com.revolgenx.anilib.data.field.media

import com.revolgenx.anilib.MediaOverViewQuery
import com.revolgenx.anilib.data.field.BaseField

class MediaOverviewField :
    BaseField<MediaOverViewQuery>() {
    var mediaId: Int? = null
    override fun toQueryOrMutation(): MediaOverViewQuery {
        return MediaOverViewQuery.builder()
            .mediaId(mediaId)
            .build()
    }

}
