package com.revolgenx.anilib.data.field

abstract class BaseField<T>() {
    abstract fun toQueryOrMutation(): T
    var canShowAdult:Boolean =  false
    companion object {
        const val PER_PAGE = 14
    }
}
