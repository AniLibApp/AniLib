package com.revolgenx.anilib.common.data.operation

import com.apollographql.apollo3.api.Optional

object OperationUtil {
    val hasUserEnabledAdult:Boolean
        get() = true

    fun nn(value: String?) = Optional.presentIfNotNull(value?.takeIf { it.isNotBlank() })
    fun <V : Any> nn(value: V?): Optional<V> = Optional.presentIfNotNull(value)
    fun <V : Any> nn(value: List<V>?): Optional<List<V>> =
        Optional.presentIfNotNull(value.takeIf { it.isNullOrEmpty().not() })
}