package com.revolgenx.anilib.data.field.list

import com.revolgenx.anilib.MediaListCollectionIdQuery
import com.revolgenx.anilib.MediaListCollectionQuery
import com.revolgenx.anilib.MediaListQuery
import com.revolgenx.anilib.UserListCountQuery
import com.revolgenx.anilib.data.field.BaseSourceUserField
import com.revolgenx.anilib.data.field.MediaListCollectionFilterField
import com.revolgenx.anilib.data.field.user.BaseUserField
import com.revolgenx.anilib.type.MediaListSort
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

class MediaListField : BaseSourceUserField<MediaListQuery>() {

    var type: Int? = null
    var status: Int? = null
    var sort: Int? = null

    override fun toQueryOrMutation(): MediaListQuery {
        return MediaListQuery.builder()
            .page(page)
            .perPage(perPage)
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

                status?.let {
                    status(MediaListStatus.values()[it])
                }

                sort?.let {
                    sort(listOf(MediaListSort.values()[it]))
                }
            }.build()
    }
}

class MediaListCountField : BaseUserField<UserListCountQuery>() {
    override fun toQueryOrMutation(): UserListCountQuery {
        return UserListCountQuery.builder()
            .userId(userId)
            .build()
    }
}
