package com.revolgenx.anilib.model.notification.thread

import com.revolgenx.anilib.model.BasicUserModel
import com.revolgenx.anilib.model.notification.NotificationModel
import com.revolgenx.anilib.model.thread.ThreadModel

open class ThreadNotification :
    NotificationModel(){
    var userModel:BasicUserModel? = null
    var threadModel: ThreadModel? = null
}