package com.revolgenx.anilib.model

import com.revolgenx.anilib.model.list.MediaListOptionModel

open class UserPrefModel() : BaseUserModel() {
    var bannerImage: String? = null
    var mediaListOption: MediaListOptionModel? = null
}