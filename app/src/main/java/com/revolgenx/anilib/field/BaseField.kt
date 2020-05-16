package com.revolgenx.anilib.field

abstract class BaseField<T>() {
    abstract fun toQueryOrMutation(): T
    var canShowAdult:Boolean =  false
    companion object {
        const val PER_PAGE = 14
    }
}
