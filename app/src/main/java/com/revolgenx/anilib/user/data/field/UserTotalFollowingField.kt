package com.revolgenx.anilib.user.data.field

import com.revolgenx.anilib.UserTotalFollowingQuery

class UserTotalFollowingField : BaseUserField<UserTotalFollowingQuery>() {
    override fun toQueryOrMutation(): UserTotalFollowingQuery {
        return UserTotalFollowingQuery(id = userId ?: -1)
    }
}