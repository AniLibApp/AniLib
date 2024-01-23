package com.revolgenx.anilib.user.data.field

import com.revolgenx.anilib.UserSocialCountQuery
import com.revolgenx.anilib.common.data.field.BaseUserField

class UserSocialCountField(override var userId: Int?) : BaseUserField<UserSocialCountQuery>() {
    override fun toQueryOrMutation(): UserSocialCountQuery {
        return UserSocialCountQuery(
            userId = userId ?: -1
        )
    }
}