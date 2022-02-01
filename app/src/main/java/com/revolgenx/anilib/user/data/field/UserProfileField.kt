package com.revolgenx.anilib.user.data.field

import com.revolgenx.anilib.UserProfileQuery

class UserProfileField : BaseUserField<UserProfileQuery>() {
    override fun toQueryOrMutation(): UserProfileQuery {
        return UserProfileQuery(id = nn(userId), name = nn(userName))
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
