package com.revolgenx.anilib.data.model

import com.revolgenx.anilib.data.model.list.MediaListOptionModel

open class UserPrefModel() : BaseUserModel() {
    var bannerImage: String? = null
    var mediaListOption: MediaListOptionModel? = null
    var displayAdultContent:Boolean? = null
}