package com.revolgenx.anilib.media.data.field

import com.revolgenx.anilib.MediaStatsQuery
import com.revolgenx.anilib.common.data.field.BaseField

class MediaStatsField : BaseField<MediaStatsQuery>(){

    var mediaId = -1
    override fun toQueryOrMutation(): MediaStatsQuery {
        return MediaStatsQuery(mediaId = nn(mediaId))
    }

}