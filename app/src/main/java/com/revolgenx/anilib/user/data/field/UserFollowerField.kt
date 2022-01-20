package com.revolgenx.anilib.user.data.field

import com.apollographql.apollo.api.Query
import com.revolgenx.anilib.UserFollowersQuery
import com.revolgenx.anilib.UserFollowingQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField


class UserFollowerField : BaseSourceField<Query<*, *, *>>() {
    var userId: Int? = null
    var isFollowing: Boolean? = null

    override fun toQueryOrMutation(): Query<*, *, *> {
        return if (isFollowing == false) {
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
