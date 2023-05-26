package com.revolgenx.anilib.common.data.tuples

import java.io.Serializable

data class MutablePair<A, B>(
    var first: A,
    var second: B
) : Serializable

infix fun <A, B> A.to(that: B): MutablePair<A, B> = MutablePair(this, that)
