package com.revolgenx.anilib.data.field.user

import com.revolgenx.anilib.UserTotalFollowersQuery

class UserTotalFollowerField : BaseUserField<UserTotalFollowersQuery>() {
    override fun toQueryOrMutation(): UserTotalFollowersQuery {
        return UserTotalFollowersQuery.builder().apply {
            userId?.let {
                id(it)
            }
        }.build()
    }
}
