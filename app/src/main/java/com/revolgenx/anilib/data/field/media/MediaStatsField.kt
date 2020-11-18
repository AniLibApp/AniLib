package com.revolgenx.anilib.data.field.media

import com.revolgenx.anilib.MediaStatsQuery
import com.revolgenx.anilib.data.field.BaseField

class MediaStatsField :BaseField<MediaStatsQuery>(){

    var mediaId = -1
    override fun toQueryOrMutation(): MediaStatsQuery {
        return MediaStatsQuery.builder()
            .mediaId(mediaId)
            .build()
    }

}