package com.revolgenx.anilib.field.list

import com.revolgenx.anilib.MediaListQuery
import com.revolgenx.anilib.field.BaseSourceUserField
import com.revolgenx.anilib.type.MediaListSort
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.MediaType

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