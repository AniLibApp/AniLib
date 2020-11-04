package com.revolgenx.anilib.data.field.notification

import com.revolgenx.anilib.NotificationQuery
import com.revolgenx.anilib.data.field.BaseSourceField

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
