package com.revolgenx.anilib.common.data.model

abstract class BaseImageModel {
    var large: String? = null
    var medium: String? = null

    val image: String
        get() {
            return large ?: medium ?: ""
        }
}