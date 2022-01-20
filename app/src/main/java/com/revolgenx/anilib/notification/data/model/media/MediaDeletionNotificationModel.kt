package com.revolgenx.anilib.notification.data.model.media

import com.revolgenx.anilib.notification.data.model.ReasonableNotificationModel

class MediaDeletionNotificationModel : ReasonableNotificationModel() {
    var deletedMediaTitle: String? = null
}