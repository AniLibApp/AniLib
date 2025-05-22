package com.revolgenx.anilib.list.data.field

import com.revolgenx.anilib.MediaListComparePageQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField
import com.revolgenx.anilib.type.MediaType

data class MediaListCompareField(
    var type: MediaType = MediaType.ANIME,
    var userId: Int = -1,
) : BaseSourceField<MediaListComparePageQuery>() {

    override fun toQueryOrMutation(): MediaListComparePageQuery {
        return MediaListComparePageQuery(
            page = nn(page),
            perPage = nn(perPage),
            userId = nn(userId),
            type = nn(type)
        )
    }
}