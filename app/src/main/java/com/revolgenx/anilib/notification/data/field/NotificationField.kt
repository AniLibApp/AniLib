package com.revolgenx.anilib.notification.data.field

import com.revolgenx.anilib.NotificationQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField
import com.revolgenx.anilib.common.data.field.BaseSourceUserField

class NotificationField : BaseSourceUserField<NotificationQuery>() {
    var resetNotificationCount = true
    var includeNotificationCount = false
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
