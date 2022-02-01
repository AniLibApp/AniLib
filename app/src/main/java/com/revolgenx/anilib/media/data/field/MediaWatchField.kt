package com.revolgenx.anilib.media.data.field

import com.revolgenx.anilib.MediaWatchQuery
import com.revolgenx.anilib.common.data.field.BaseField

class MediaWatchField :
    BaseField<MediaWatchQuery>() {
    var mediaId = -1
    override fun toQueryOrMutation(): MediaWatchQuery {
        return MediaWatchQuery(mediaId = nn(mediaId))
    }
}