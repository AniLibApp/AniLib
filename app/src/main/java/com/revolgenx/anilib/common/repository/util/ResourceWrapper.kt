package com.revolgenx.anilib.common.repository.util

import java.lang.Exception

sealed class ResourceWrapper<out T> {
    data class Loading<out T>(val value: T? = null) : ResourceWrapper<Nothing>()
    data class Success<out T>(val value: T) : ResourceWrapper<T>()
    data class Error<out T>(
        var errorMsg: String?,
        val causes: Map<String, String>? = null,
        val exception: Exception? = null,
        val value: T? = null
    ) : ResourceWrapper<Nothing>()
}
