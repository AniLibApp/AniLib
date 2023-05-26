package com.revolgenx.anilib.user.data.field

import com.revolgenx.anilib.UserQuery
import com.revolgenx.anilib.common.data.field.BaseUserField

class UserField : BaseUserField<UserQuery>() {
    override fun toQueryOrMutation(): UserQuery {
        return UserQuery(
            id = nn(userId),
            name = nn(userName),
            userId = userId ?: -1
        )
    }
}