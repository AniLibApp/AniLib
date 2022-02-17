package com.revolgenx.anilib.user.data.field

import com.revolgenx.anilib.UserFollowingFollowerCountQuery

class UserFollowingFollowerCountField : BaseUserField<UserFollowingFollowerCountQuery>() {
    override fun toQueryOrMutation(): UserFollowingFollowerCountQuery {
        return UserFollowingFollowerCountQuery(userId = userId ?: -1)
    }
}