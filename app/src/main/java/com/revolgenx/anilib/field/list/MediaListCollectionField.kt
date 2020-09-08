package com.revolgenx.anilib.field.list

import com.revolgenx.anilib.MediaListCollectionQuery
import com.revolgenx.anilib.field.BaseSourceUserField
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.MediaType

class MediaListCollectionField : BaseSourceUserField<MediaListCollectionQuery>() {
    var type: Int? = null
    var mediaListStatus: Int? = null

    override fun toQueryOrMutation(): MediaListCollectionQuery {
        return MediaListCollectionQuery.builder()
            .apply {
                userId?.let {
                    userId(it)
                }
                userName?.let {
                    userName(it)
                }
                type?.let {
                    type(MediaType.values()[it])
                }
                mediaListStatus?.let {
                    status(MediaListStatus.values()[it])
                }
            }.build()
    }
}
