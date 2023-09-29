package com.revolgenx.anilib.setting.data.field

import com.revolgenx.anilib.UserNotificationSettingQuery
import com.revolgenx.anilib.common.data.field.BaseUserField

class UserNotificationSettingsField : BaseUserField<UserNotificationSettingQuery>() {
    override fun toQueryOrMutation(): UserNotificationSettingQuery {
        return UserNotificationSettingQuery(id = nn(userId), name = nn(userName))
    }

}