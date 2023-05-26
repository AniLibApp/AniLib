package com.revolgenx.anilib.list.data.field

import com.revolgenx.anilib.MediaListCollectionIdQuery
import com.revolgenx.anilib.common.data.field.BaseUserField
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.MediaType

data class MediaListCollectionIdField(
    val type: MediaType = MediaType.ANIME,
    val mediaListStatus: List<MediaListStatus>? = null
) : BaseUserField<MediaListCollectionIdQuery>() {

    override fun toQueryOrMutation(): MediaListCollectionIdQuery {
        return MediaListCollectionIdQuery(
            userId = nn(userId),
            userName = nn(userName),
            type = nn(type),
            status_in = nn(mediaListStatus)
        )
    }
}