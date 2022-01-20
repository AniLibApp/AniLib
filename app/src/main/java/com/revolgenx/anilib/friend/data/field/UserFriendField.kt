package com.revolgenx.anilib.friend.data.field

import com.apollographql.apollo.api.Query
import com.revolgenx.anilib.UserFollowersQuery
import com.revolgenx.anilib.UserFollowingQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField

class UserFriendField : BaseSourceField<Query<*, *, *>>() {
    var userId: Int? = null
    var isFollower: Boolean = false

    override fun toQueryOrMutation(): Query<*, *, *> {
        return if (isFollower) {
            UserFollowersQuery.builder()
                .apply {
                    userId?.let {
                        id(it)
                    }
                    page(page)
                    perPage(perPage)
                }
                .build()
        } else {
            UserFollowingQuery.builder()
                .apply {
                    userId?.let {
                        id(it)
                    }
                    page(page)
                    perPage(perPage)
                }.build()
        }
    }
}
