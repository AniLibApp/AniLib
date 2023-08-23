package com.revolgenx.anilib.media.data.field

import com.revolgenx.anilib.MediaOverviewQuery
import com.revolgenx.anilib.common.data.field.BaseField

class MediaOverviewField(var mediaId: Int = -1) : BaseField<MediaOverviewQuery>() {
    override fun toQueryOrMutation(): MediaOverviewQuery {
        return MediaOverviewQuery(mediaId = nn(mediaId))
    }
}

