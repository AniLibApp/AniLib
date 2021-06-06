package com.revolgenx.anilib.data.model.notification.activity

import com.revolgenx.anilib.data.model.user.UserPrefModel
import com.revolgenx.anilib.data.model.activity.ListActivityModel
import com.revolgenx.anilib.data.model.activity.MessageActivityModel
import com.revolgenx.anilib.data.model.activity.TextActivityModel
import com.revolgenx.anilib.data.model.notification.NotificationModel

open class ActivityNotification : NotificationModel() {
    var activityId: Int? = null
    var userPrefModel: UserPrefModel? = null
    var textActivityModel: TextActivityModel? = null
    var listActivityModel: ListActivityModel? = null
    var messageActivityModel: MessageActivityModel? = null
}