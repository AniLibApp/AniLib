package com.revolgenx.anilib.data.field.notification

import com.revolgenx.anilib.NotificationQuery
import com.revolgenx.anilib.UserNotificationSettingMutation
import com.revolgenx.anilib.UserNotificationSettingQuery
import com.revolgenx.anilib.data.field.BaseField
import com.revolgenx.anilib.data.field.BaseSourceField
import com.revolgenx.anilib.data.field.user.BaseUserField
import com.revolgenx.anilib.type.NotificationOptionInput
import com.revolgenx.anilib.type.NotificationType

class NotificationField : BaseSourceField<NotificationQuery>() {
    var resetNotificationCount = true
    override fun toQueryOrMutation(): NotificationQuery {
        return NotificationQuery
            .builder()
            .page(page)
            .perPage(perPage)
            .resetNotificationCount(resetNotificationCount)
            .build()
    }
}

class UserNotificationMutateField : BaseField<UserNotificationSettingMutation>() {
    var notificationSettings: Map<NotificationType, Boolean>? = null

    override fun toQueryOrMutation(): UserNotificationSettingMutation {
        return UserNotificationSettingMutation.builder()
            .notificationOptions(notificationSettings?.map { NotificationOptionInput.builder().type(it.key).enabled(it.value).build() })
            .build()
    }

}

class UserNotificationSettingField : BaseUserField<UserNotificationSettingQuery>() {
    override fun toQueryOrMutation(): UserNotificationSettingQuery {
        return UserNotificationSettingQuery.builder()
            .apply {
                userId?.let {
                    id(it)
                }
                userName?.let {
                    name(it)
                }
            }.build()
    }

}