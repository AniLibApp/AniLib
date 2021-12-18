package com.revolgenx.anilib.data.model.notification.media

import com.revolgenx.anilib.data.model.CommonMediaModel
import com.revolgenx.anilib.data.model.notification.ReasonableNotificationModel

class MediaDataChangeNotificationModel: ReasonableNotificationModel() {
    var media: CommonMediaModel? = null
}