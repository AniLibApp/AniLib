package com.revolgenx.anilib.social.data.data

import com.revolgenx.anilib.social.data.field.ActivityUnionField
import com.revolgenx.anilib.type.ActivityType
import kotlinx.serialization.Serializable

@Serializable
data class ActivityUnionFilterData(
    val isFollowing: Boolean = false,
    val type: ActivityType? = null,
) {
    fun toField(): ActivityUnionField {
        return ActivityUnionField(
            isFollowing = isFollowing,
            type = type,
        )
    }
}