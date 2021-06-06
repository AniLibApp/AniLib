package com.revolgenx.anilib.data.model.notification.thread

import com.revolgenx.anilib.data.model.user.UserPrefModel
import com.revolgenx.anilib.data.model.notification.NotificationModel
import com.revolgenx.anilib.data.model.thread.ThreadModel

open class ThreadNotification :
    NotificationModel(){
    var userPrefModel: UserPrefModel? = null
    var threadModel: ThreadModel? = null
}