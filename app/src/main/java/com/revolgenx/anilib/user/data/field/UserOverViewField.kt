package com.revolgenx.anilib.user.data.field

import com.revolgenx.anilib.UserOverViewQuery


class UserOverViewField : BaseUserField<UserOverViewQuery>() {
    var includeFollow = userId != null

    override fun toQueryOrMutation(): UserOverViewQuery {
        return UserOverViewQuery(
            includeFollow = includeFollow,
            id = nn(userId),
            name = nn(userName),
            userId = userId ?: -1
        )
    }
}
