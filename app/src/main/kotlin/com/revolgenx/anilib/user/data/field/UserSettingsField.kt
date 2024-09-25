package com.revolgenx.anilib.user.data.field

import com.revolgenx.anilib.UserSettingsQuery
import com.revolgenx.anilib.common.data.field.BaseUserField

class UserSettingsField : BaseUserField<UserSettingsQuery>() {
    override fun toQueryOrMutation(): UserSettingsQuery {
        return UserSettingsQuery(id = nn(userId))
    }
}