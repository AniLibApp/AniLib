package com.revolgenx.anilib.notification.data.model.media

import com.revolgenx.anilib.common.data.model.CommonMediaModel
import com.revolgenx.anilib.notification.data.model.ReasonableNotificationModel

class MediaMergeNotificationModel:ReasonableNotificationModel() {
    var media: CommonMediaModel? = null
    var deletedMediaTitles:List<String>? = null
}