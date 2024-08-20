package com.revolgenx.anilib.list.data.field

import com.revolgenx.anilib.MediaListPageQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField
import com.revolgenx.anilib.type.MediaListSort
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.MediaType

data class MediaListField(
    val type: MediaType,
    val status: MediaListStatus,
    var sort: MediaListSort? = null,
    var userId: Int = -1,
): BaseSourceField<MediaListPageQuery>() {
    override fun toQueryOrMutation(): MediaListPageQuery {
        return MediaListPageQuery(
            page = nn(page),
            perPage = nn(perPage),
            userId = nn(userId),
            type = nn(type),
            status = nn(status),
            sort = nn(sort?.let { listOf(sort) })
        )
    }
}