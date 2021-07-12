package com.revolgenx.anilib.data.model.user

class AvatarModel() {
    var large: String? = null
    var medium: String? = null

    val image: String
        get() = large ?: medium ?: ""
}