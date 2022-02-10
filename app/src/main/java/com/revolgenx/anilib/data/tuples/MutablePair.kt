package com.revolgenx.anilib.data.tuples

import java.io.Serializable

data class MutablePair<A, B>(
    var first: A,
    var second: B
) : Serializable {
}
