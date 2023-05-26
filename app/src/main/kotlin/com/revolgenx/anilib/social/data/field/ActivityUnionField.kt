package com.revolgenx.anilib.social.data.field

import com.revolgenx.anilib.ActivityUnionQuery
import com.revolgenx.anilib.common.data.field.BaseSourceUserField
import com.revolgenx.anilib.type.ActivityType

class ActivityUnionField : BaseSourceUserField<ActivityUnionQuery>() {
    var isFollowing: Boolean = false
    var type: ActivityType? = null
    var mediaId: Int? = null
    override fun toQueryOrMutation(): ActivityUnionQuery {
        val typeIn = if (userId == null) {
            listOf(
                ActivityType.TEXT,
                ActivityType.MEDIA_LIST,
                ActivityType.ANIME_LIST,
                ActivityType.MANGA_LIST
            )
        } else {
            null
        }

        return ActivityUnionQuery(
            page = nn(page),
            perPage = nn(perPage),
            userId = nn(userId),
            mediaId = nn(mediaId),
            isFollowing = nn(if (isFollowing) true else null),
            type = nn(type),
            type_in = nn(typeIn)
        )
    }
}