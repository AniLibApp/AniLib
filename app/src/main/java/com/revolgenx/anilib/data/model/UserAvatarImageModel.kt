package com.revolgenx.anilib.data.model

class UserAvatarImageModel() {
    var large: String? = null
    var medium: String? = null

    val image: String
        get() = large ?: medium ?: ""
}