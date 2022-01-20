package com.revolgenx.anilib.notification.data.model.activity

import com.revolgenx.anilib.notification.data.model.ListActivityModel
import com.revolgenx.anilib.notification.data.model.MessageActivityModel
import com.revolgenx.anilib.notification.data.model.TextActivityModel
import com.revolgenx.anilib.notification.data.model.NotificationModel
import com.revolgenx.anilib.user.data.model.UserModel

open class ActivityNotification : NotificationModel() {
    var activityId: Int? = null
    var user: UserModel? = null
    var textActivityModel: TextActivityModel? = null
    var listActivityModel: ListActivityModel? = null
    var messageActivityModel: MessageActivityModel? = null
}