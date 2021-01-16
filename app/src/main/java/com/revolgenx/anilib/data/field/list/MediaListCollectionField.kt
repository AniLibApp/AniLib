package com.revolgenx.anilib.data.field.list

import com.revolgenx.anilib.MediaListCollectionIdQuery
import com.revolgenx.anilib.MediaListCollectionQuery
import com.revolgenx.anilib.data.field.BaseSourceUserField
import com.revolgenx.anilib.data.field.MediaListCollectionFilterField
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.MediaType

class MediaListCollectionField : BaseSourceUserField<MediaListCollectionQuery>() {
    var type: Int? = null
    var mediaListStatus: Int? = null
    var filter = MediaListCollectionFilterField()

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


class MediaListCollectionIdsField : BaseSourceUserField<MediaListCollectionIdQuery>() {
    var type: Int? = null
    var mediaListStatus: List<Int>? = null

    override fun toQueryOrMutation(): MediaListCollectionIdQuery {
        return MediaListCollectionIdQuery.builder()
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
                    status_in(it.map { MediaListStatus.values()[it] })
                }
            }.build()
    }
}