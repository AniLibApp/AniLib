package com.revolgenx.anilib.social.data.field

import com.revolgenx.anilib.ActivityUnionQuery
import com.revolgenx.anilib.common.data.field.BaseSourceUserField
import com.revolgenx.anilib.constant.AlActivityType
import com.revolgenx.anilib.type.ActivityType

open class ActivityUnionField : BaseSourceUserField<ActivityUnionQuery>() {

    var isFollowing: Boolean = false
    var type: AlActivityType = AlActivityType.ALL
    var mediaId: Int? = null
    override fun toQueryOrMutation(): ActivityUnionQuery {
        val mType = when (type) {
            AlActivityType.TEXT -> ActivityType.TEXT
            AlActivityType.LIST -> ActivityType.MEDIA_LIST
            AlActivityType.MESSAGE -> ActivityType.MESSAGE
            else -> null
        }

        val typeIn = if (userId == null) {
            listOf(
                ActivityType.MEDIA_LIST,
                ActivityType.TEXT,
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
            type = nn(mType),
            type_in = nn(typeIn)
        )
    }

}
