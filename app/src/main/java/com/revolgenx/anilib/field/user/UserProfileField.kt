package com.revolgenx.anilib.field.user

import com.revolgenx.anilib.UserProfileQuery

class UserProfileField : BaseUserField<UserProfileQuery>() {
    override fun toQueryOrMutation(): UserProfileQuery {
        return UserProfileQuery.builder().apply {
            userId?.let {
                id(userId)
            }
            userName?.let {
                name(userName)
            }
        }.build()
    }

    val userTotalFollowingField: UserTotalFollowingField
        get() = UserTotalFollowingField().also {
            it.userId = userId
        }

    val userTotalFollowerField: UserTotalFollowerField
        get() = UserTotalFollowerField().also {
            it.userId = userId
        }
}
