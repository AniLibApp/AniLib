package com.revolgenx.anilib.entry.data.field

import com.revolgenx.anilib.MediaListEntryQuery
import com.revolgenx.anilib.common.data.field.BaseUserField

class MediaListEntryField : BaseUserField<MediaListEntryQuery>() {
    var mediaId: Int = -1
    override fun toQueryOrMutation(): MediaListEntryQuery {
        return MediaListEntryQuery(mediaId = nn(mediaId), userId = nn(userId))
    }
}