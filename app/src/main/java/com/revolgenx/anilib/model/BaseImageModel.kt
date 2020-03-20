package com.revolgenx.anilib.model

open class BaseImageModel {
    var large: String? = null
    var medium: String? = null

    val image: String
        get() {
            return large ?: medium ?: ""
        }
}
