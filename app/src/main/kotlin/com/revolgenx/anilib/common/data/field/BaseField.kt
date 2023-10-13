package com.revolgenx.anilib.common.data.field

import com.apollographql.apollo3.api.Optional

abstract class BaseField<T> {
    abstract fun toQueryOrMutation(): T
    var canShowAdult: Boolean = false
    protected fun <V> nn(value: V?): Optional<V> = Optional.presentIfNotNull(value)
    protected fun <V> nn(value: List<V>?): Optional<List<V>> =
        Optional.presentIfNotNull(value?.takeIf { it.isEmpty().not() })
    protected fun <V> nnList(value: List<V>?): Optional<List<V>> =
        Optional.presentIfNotNull(value)

    protected fun nnString(value: String?) =
        Optional.presentIfNotNull(value?.takeIf { it.isNotBlank() })

    protected fun nnBool(value: Boolean?): Optional<Boolean> =
        Optional.presentIfNotNull(value?.takeIf { it })
}
