package com.revolgenx.anilib.common.data.state

sealed class ResourceState<T> {
    data class Loading<T>(val data: T?) : ResourceState<T>()
    data class Success<T>(val data: T?) : ResourceState<T>()
    data class Error<T>(
        var message: String?,
        val causes: Map<String, String>? = null,
        val error: Throwable? = null,
        val data: T?
    ) : ResourceState<T>()

    companion object {

        fun <T> success(data: T?): ResourceState<T> {
            return Success(data)
        }

        fun <T> error(error: Throwable? = null, data: T? = null): ResourceState<T> =
            Error(error?.message ?: "Unknown error", error = error, data = data)

        fun <T> error(
            msg: String?,
            data: T? = null,
            exception: Throwable? = null
        ): ResourceState<T> =
            Error(
                msg ?: "Unknown Error",
                error = exception,
                data = data
            )

        fun <T> loading(data: T? = null): ResourceState<T> = Loading(data)
    }
}