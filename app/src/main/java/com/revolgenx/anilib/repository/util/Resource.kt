package com.revolgenx.anilib.repository.util

data class Resource<out T>(
    val status: Status,
    val data: T?,
    val message: String?,
    val code: Int = -1
) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(
                Status.SUCCESS,
                data,
                null
            )
        }

        fun <T> error(msg: String, data: T?, code: Int = -1): Resource<T> {
            return Resource(
                Status.ERROR,
                data,
                msg
            )
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(
                Status.LOADING,
                data,
                null
            )
        }
    }
}

val ERROR = "ERROR"

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}