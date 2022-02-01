package com.revolgenx.anilib.list.data.field

import com.revolgenx.anilib.MediaListCollectionQuery
import com.revolgenx.anilib.common.data.field.BaseSourceUserField
import com.revolgenx.anilib.type.MediaType

class MediaListCollectionField : BaseSourceUserField<MediaListCollectionQuery>() {
    var type: MediaType = MediaType.ANIME
    override fun toQueryOrMutation(): MediaListCollectionQuery {
        return MediaListCollectionQuery(
            userId = nn(userId),
            userName = nn(userName),
            type = nn(type)
        )
    }
}