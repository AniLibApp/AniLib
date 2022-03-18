package com.revolgenx.anilib.common.data.field

import com.apollographql.apollo3.api.Optional
import com.revolgenx.anilib.common.preference.canShowAdult

abstract class BaseField<T> {
    abstract fun toQueryOrMutation(): T

    val canShowAdult: Boolean
        get() = canShowAdult()

    protected fun nn(value: String?) = Optional.presentIfNotNull(value?.takeIf { it.isNotBlank() })
    protected fun <V : Any> nn(value: V?): Optional<V> = Optional.presentIfNotNull(value)
    protected fun <V : Any> nn(value: List<V>?): Optional<List<V>> =
        Optional.presentIfNotNull(value?.takeIf { it.isNullOrEmpty().not() })

    protected fun <V : Any> nnList(value: List<V>?): Optional<List<V>> =
        Optional.presentIfNotNull(value)

    protected fun nnBool(value: Boolean?): Optional<Boolean> =
        Optional.presentIfNotNull(value?.takeIf { it })

}
