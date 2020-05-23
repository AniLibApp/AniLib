package com.revolgenx.anilib.field.user

import com.revolgenx.anilib.UserTotalFollowingQuery

class UserTotalFollowingField : BaseUserField<UserTotalFollowingQuery>() {
    override fun toQueryOrMutation(): UserTotalFollowingQuery {
        return UserTotalFollowingQuery.builder().apply {
            userId?.let {
                id(it)
            }
        }.build()
    }
}