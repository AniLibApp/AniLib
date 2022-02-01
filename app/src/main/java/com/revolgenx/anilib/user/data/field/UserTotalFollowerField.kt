package com.revolgenx.anilib.user.data.field

import com.revolgenx.anilib.UserTotalFollowersQuery

class UserTotalFollowerField : BaseUserField<UserTotalFollowersQuery>() {
    override fun toQueryOrMutation(): UserTotalFollowersQuery {
        return UserTotalFollowersQuery(id = userId ?: -1)
    }
}
