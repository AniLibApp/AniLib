package com.revolgenx.anilib.common.ui.model

abstract class BaseImageModel(private val medium: String?, private val large: String?) {
    val image: String
        get() = large ?: medium ?: ""
}