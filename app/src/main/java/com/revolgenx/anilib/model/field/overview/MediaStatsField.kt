package com.revolgenx.anilib.model.field.overview

import com.revolgenx.anilib.MediaStatsQuery
import com.revolgenx.anilib.model.field.BaseField

class MediaStatsField :BaseField<MediaStatsQuery>{

    var mediaId = -1
    override fun toQuery(): MediaStatsQuery {
        return MediaStatsQuery.builder()
            .mediaId(mediaId)
            .build()
    }

}