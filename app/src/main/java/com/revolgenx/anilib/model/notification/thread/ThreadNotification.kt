package com.revolgenx.anilib.model.notification.thread

import com.revolgenx.anilib.model.UserPrefModel
import com.revolgenx.anilib.model.notification.NotificationModel
import com.revolgenx.anilib.model.thread.ThreadModel

open class ThreadNotification :
    NotificationModel(){
    var userPrefModel:UserPrefModel? = null
    var threadModel: ThreadModel? = null
}