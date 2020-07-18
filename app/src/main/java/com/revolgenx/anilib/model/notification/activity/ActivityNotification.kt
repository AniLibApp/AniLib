package com.revolgenx.anilib.model.notification.activity

import com.revolgenx.anilib.model.UserPrefModel
import com.revolgenx.anilib.model.activity.ListActivityModel
import com.revolgenx.anilib.model.activity.MessageActivityModel
import com.revolgenx.anilib.model.activity.TextActivityModel
import com.revolgenx.anilib.model.notification.NotificationModel

open class ActivityNotification : NotificationModel() {
    var activityId: Int? = null
    var userPrefModel: UserPrefModel? = null
    var textActivityModel: TextActivityModel? = null
    var listActivityModel: ListActivityModel? = null
    var messageActivityModel: MessageActivityModel? = null
}