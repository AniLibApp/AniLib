package com.revolgenx.anilib.list.data.field

import com.revolgenx.anilib.MediaListCollectionQuery
import com.revolgenx.anilib.common.data.field.BaseUserField
import com.revolgenx.anilib.type.MediaType

class MediaListCollectionField(var type: MediaType) : BaseUserField<MediaListCollectionQuery>() {
    override fun toQueryOrMutation(): MediaListCollectionQuery {
        return MediaListCollectionQuery(
            userId = nn(userId),
            userName = nn(userName),
            type = nn(type),
            isAnime = type == MediaType.ANIME
        )
    }
}