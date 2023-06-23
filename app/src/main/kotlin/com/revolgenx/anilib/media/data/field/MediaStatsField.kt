package com.revolgenx.anilib.media.data.field

import com.revolgenx.anilib.MediaStatsQuery
import com.revolgenx.anilib.common.data.field.BaseField

data class MediaStatsField(var mediaId: Int = -1) : BaseField<MediaStatsQuery>() {
    override fun toQueryOrMutation(): MediaStatsQuery {
        return MediaStatsQuery(mediaId = nn(mediaId))
    }

}