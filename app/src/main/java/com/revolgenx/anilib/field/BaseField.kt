package com.revolgenx.anilib.field

interface BaseField<T> {

    fun toQueryOrMutation(): T

    companion object {
        const val PER_PAGE = 14
    }
}
