package com.revolgenx.anilib.notification.data.model.media

import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.notification.data.model.ReasonableNotificationModel

class MediaDataChangeNotificationModel: ReasonableNotificationModel() {
    var media: MediaModel? = null
}