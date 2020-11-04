package com.revolgenx.anilib.data.model.notification.activity

import com.revolgenx.anilib.data.model.CommonMediaModel
import com.revolgenx.anilib.data.model.notification.NotificationModel

class AiringNotificationModel : NotificationModel() {
    var episode: Int? = null
    var commonMediaModel: CommonMediaModel? = null
}