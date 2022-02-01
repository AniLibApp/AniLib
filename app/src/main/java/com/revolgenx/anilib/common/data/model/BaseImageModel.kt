package com.revolgenx.anilib.common.data.model

abstract class BaseImageModel(val medium: String?, val large: String?) {
    val image: String
        get() {
            return large ?: medium ?: ""
        }
}