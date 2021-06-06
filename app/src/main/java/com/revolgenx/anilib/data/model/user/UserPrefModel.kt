package com.revolgenx.anilib.data.model.user

import com.revolgenx.anilib.data.model.setting.MediaListOptionModel
import com.revolgenx.anilib.data.model.setting.MediaOptionModel
import com.revolgenx.anilib.data.model.user.UserModel

open class UserPrefModel() : UserModel() {
    var bannerImage: String? = null
    var mediaListOption: MediaListOptionModel? = null
    var mediaOptions:MediaOptionModel? = null
}