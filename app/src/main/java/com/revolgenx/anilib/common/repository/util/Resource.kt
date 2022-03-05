package com.revolgenx.anilib.common.repository.util

sealed class Resource<out T>(val data: T?) {
    data class Success<out T>(val value: T?) : Resource<T>(value)
    data class Loading<out T>(val value: T? = null) : Resource<T>(value)
    data class Error<out T>(
        var message: String?,
        val causes: Map<String, String>? = null,
        val exception: Throwable? = null,
        val value: T? = null
    ) : Resource<T>(value)

    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Success(data)
        }

        fun <T> error(exception: Throwable? = null, data: T? = null): Resource<T> =
            Error(exception?.message ?: "Unknown error", exception = exception, value = data)

        fun <T> error(msg: String?, data: T? = null, exception: Throwable? = null): Resource<T> {
            return Error(
                msg ?: "Unknown Error",
                exception = exception,
                value = data
            )
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Loading(data)
        }
    }
}

