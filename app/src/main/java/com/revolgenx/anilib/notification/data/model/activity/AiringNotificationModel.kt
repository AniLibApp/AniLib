package com.revolgenx.anilib.notification.data.model.activity

import com.revolgenx.anilib.common.data.model.CommonMediaModel
import com.revolgenx.anilib.notification.data.model.NotificationModel

class AiringNotificationModel : NotificationModel() {
    var episode: Int? = null
    var commonMediaModel: CommonMediaModel? = null
}