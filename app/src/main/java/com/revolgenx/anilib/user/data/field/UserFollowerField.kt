package com.revolgenx.anilib.user.data.field

import com.apollographql.apollo3.api.Query
import com.revolgenx.anilib.UserFollowersQuery
import com.revolgenx.anilib.UserFollowingQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField


class UserFollowerField : BaseSourceField<Query<*>>() {
    var userId: Int? = null
    var isFollowing: Boolean? = null

    override fun toQueryOrMutation(): Query<*> {
        return if (isFollowing == false) {
            UserFollowersQuery(id = userId ?: -1, page = nn(page), perPage = nn(perPage))
        } else {
            UserFollowingQuery(id = userId ?: -1, page = nn(page), perPage = nn(perPage))
        }
    }
}
