package com.revolgenx.anilib.friend.data.field

import com.apollographql.apollo3.api.Query
import com.revolgenx.anilib.UserFollowersQuery
import com.revolgenx.anilib.UserFollowingQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField

class UserFriendField : BaseSourceField<Query<*>>() {
    var userId: Int? = null
    var isFollower: Boolean = false

    override fun toQueryOrMutation(): Query<*> {
        return if (isFollower) {
            UserFollowersQuery(id = userId ?: -1, page = nn(page), perPage = nn(perPage))
        } else {
            UserFollowingQuery(id = userId ?: -1, page = nn(page), perPage = nn(perPage))
        }
    }
}
