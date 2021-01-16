package com.revolgenx.anilib.infrastructure.repository.util

data class Resource<T>(
    val status: Status,
    val data: T?,
    val message: String?,
    val exception: Throwable? = null
) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(
                Status.SUCCESS,
                data,
                null
            )
        }

        fun <T> error(msg: String, data: T?, exception: Throwable? = null): Resource<T> {
            return Resource(
                Status.ERROR,
                data,
                msg,
                exception
            )
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(
                Status.LOADING,
                data,
                null
            )
        }
    }
}

const val ERROR = "ERROR"

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}