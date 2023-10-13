package com.revolgenx.anilib.notification.data.field

import com.revolgenx.anilib.NotificationQuery
import com.revolgenx.anilib.common.data.field.BaseSourceUserField

data class NotificationField(
    val resetNotificationCount: Boolean = true,
    var includeNotificationCount: Boolean = false
) : BaseSourceUserField<NotificationQuery>() {

    var unreadNotificationCount: Int? = null

    override fun toQueryOrMutation(): NotificationQuery {
        return NotificationQuery(
            page = nn(page),
            perPage = nn(perPage),
            userId = nn(userId),
            includeNotificationCount = includeNotificationCount,
            resetNotificationCount = nn(resetNotificationCount)
        )
    }
}
