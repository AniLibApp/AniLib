package com.revolgenx.anilib.field.overview

import com.revolgenx.anilib.MediaStatsQuery
import com.revolgenx.anilib.field.BaseField

class MediaStatsField :BaseField<MediaStatsQuery>{

    var mediaId = -1
    override fun toQueryOrMutation(): MediaStatsQuery {
        return MediaStatsQuery.builder()
            .mediaId(mediaId)
            .build()
    }

}