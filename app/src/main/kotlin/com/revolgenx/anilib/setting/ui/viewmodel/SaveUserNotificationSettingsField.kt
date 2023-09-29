package com.revolgenx.anilib.setting.ui.viewmodel

import com.revolgenx.anilib.UserNotificationSettingMutation
import com.revolgenx.anilib.common.data.field.BaseField
import com.revolgenx.anilib.type.NotificationOptionInput
import com.revolgenx.anilib.type.NotificationType

data class SaveUserNotificationSettingsField(val notificationSettings: Map<NotificationType, Boolean>) :
    BaseField<UserNotificationSettingMutation>() {
    override fun toQueryOrMutation(): UserNotificationSettingMutation {
        return UserNotificationSettingMutation(
            notificationOptions = nn(notificationSettings.map { NotificationOptionInput(type = nn(it.key), enabled = nn(it.value)) })
        )
    }
}