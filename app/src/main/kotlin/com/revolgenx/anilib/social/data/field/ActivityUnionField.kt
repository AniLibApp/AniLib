package com.revolgenx.anilib.social.data.field

import com.revolgenx.anilib.ActivityUnionQuery
import com.revolgenx.anilib.common.data.field.BaseSourceUserField
import com.revolgenx.anilib.social.data.data.ActivityUnionFilterData
import com.revolgenx.anilib.type.ActivityType

data class ActivityUnionField(
    var isFollowing: Boolean? = false,
    var type: ActivityType? = null,
    var mediaId: Int? = null
) : BaseSourceUserField<ActivityUnionQuery>() {
    override var perPage: Int = 25
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
            hasReplies = nnBool(isFollowing?.let { !it }),
            isFollowing = nnBool(isFollowing),
            type = nn(type),
            type_in = nn(typeIn)
        )
    }


    fun toFilterData(): ActivityUnionFilterData {
        return ActivityUnionFilterData(
            isFollowing = isFollowing!!,
            type = type,
        )
    }
}