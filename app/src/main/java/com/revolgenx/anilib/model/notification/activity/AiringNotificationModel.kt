package com.revolgenx.anilib.model.notification.activity

import com.revolgenx.anilib.model.CommonMediaModel
import com.revolgenx.anilib.model.notification.NotificationModel

class AiringNotificationModel : NotificationModel() {
    var episode: Int? = null
    var commonMediaModel: CommonMediaModel? = null
}