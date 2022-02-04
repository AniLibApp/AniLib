package com.revolgenx.anilib.notification.data.model.activity

import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.notification.data.model.NotificationModel

class AiringNotificationModel : NotificationModel() {
    var episode: Int? = null
    var media: MediaModel? = null
}