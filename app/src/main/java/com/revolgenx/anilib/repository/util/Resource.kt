package com.revolgenx.anilib.repository.util

import java.lang.Exception

data class Resource<out T>(
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