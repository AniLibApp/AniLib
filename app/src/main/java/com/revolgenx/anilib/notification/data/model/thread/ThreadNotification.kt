package com.revolgenx.anilib.notification.data.model.thread

import com.revolgenx.anilib.notification.data.model.ThreadModel
import com.revolgenx.anilib.notification.data.model.NotificationModel
import com.revolgenx.anilib.user.data.model.UserModel

open class ThreadNotification : NotificationModel() {
    var user: UserModel? = null
    var threadModel: ThreadModel? = null
}