package com.revolgenx.anilib.common.data.field

import com.apollographql.apollo3.api.Optional
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

abstract class BaseField<T> {
    abstract fun toQueryOrMutation(): T
    val hasUserEnabledAdult: Boolean
        get() = true

    protected fun nn(value: String?) = Optional.presentIfNotNull(value?.takeIf { it.isNotBlank() })
    protected fun <V : Any> nn(value: V?): Optional<V> = Optional.presentIfNotNull(value)
    protected fun <V : Any> nn(value: List<V>?): Optional<List<V>> =
        Optional.presentIfNotNull(value?.takeIf { it.isEmpty().not() })

    protected fun <V : Any> nnList(value: List<V>?): Optional<List<V>> =
        Optional.presentIfNotNull(value)

    protected fun nnBool(value: Boolean?): Optional<Boolean> =
        Optional.presentIfNotNull(value?.takeIf { it })
}