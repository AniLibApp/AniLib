package com.revolgenx.anilib.common.util

import com.revolgenx.anilib.type.MediaType

typealias OnClick = () -> Unit
typealias OnClickWithValue<M> = (M) -> Unit
typealias OnMediaClick = (id: Int, type: MediaType?) -> Unit

typealias OnLongClick = () -> Unit
typealias OnLongClickWithValue<M> = (M) -> Unit