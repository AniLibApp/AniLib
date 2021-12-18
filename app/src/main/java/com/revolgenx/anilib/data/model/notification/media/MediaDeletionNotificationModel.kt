package com.revolgenx.anilib.data.model.notification.media

import com.revolgenx.anilib.data.model.notification.ReasonableNotificationModel

class MediaDeletionNotificationModel : ReasonableNotificationModel() {
    var deletedMediaTitle: String? = null
}