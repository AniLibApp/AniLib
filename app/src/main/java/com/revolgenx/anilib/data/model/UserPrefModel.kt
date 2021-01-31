package com.revolgenx.anilib.data.model

import com.revolgenx.anilib.data.model.setting.MediaListOptionModel
import com.revolgenx.anilib.data.model.setting.MediaOptionModel

open class UserPrefModel() : BaseUserModel() {
    var bannerImage: String? = null
    var mediaListOption: MediaListOptionModel? = null
    var mediaOptions:MediaOptionModel? = null
}