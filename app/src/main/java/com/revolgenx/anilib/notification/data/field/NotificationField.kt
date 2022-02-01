package com.revolgenx.anilib.notification.data.field

import com.revolgenx.anilib.NotificationQuery
import com.revolgenx.anilib.UserNotificationSettingMutation
import com.revolgenx.anilib.UserNotificationSettingQuery
import com.revolgenx.anilib.common.data.field.BaseField
import com.revolgenx.anilib.common.data.field.BaseSourceField
import com.revolgenx.anilib.user.data.field.BaseUserField
import com.revolgenx.anilib.type.NotificationOptionInput
import com.revolgenx.anilib.type.NotificationType

class NotificationField : BaseSourceField<NotificationQuery>() {
    var resetNotificationCount = true
    override fun toQueryOrMutation(): NotificationQuery {
        return NotificationQuery(
            page = nn(page),
            perPage = nn(perPage),
            resetNotificationCount = nn(resetNotificationCount)
        )
    }
}

class UserNotificationMutateField : BaseField<UserNotificationSettingMutation>() {
    var notificationSettings: Map<NotificationType, Boolean>? = null

    override fun toQueryOrMutation(): UserNotificationSettingMutation {

        return UserNotificationSettingMutation(
            notificationOptions = nn(notificationSettings
                ?.map { NotificationOptionInput(type = nn(it.key), enabled = nn(it.value)) })
        )
    }

}

class UserNotificationSettingField : BaseUserField<UserNotificationSettingQuery>() {
    override fun toQueryOrMutation(): UserNotificationSettingQuery {
        return UserNotificationSettingQuery(id = nn(userId), name = nn(userName))
    }

}