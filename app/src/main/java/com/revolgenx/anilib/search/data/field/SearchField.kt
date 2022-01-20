package com.revolgenx.anilib.search.data.field

import com.revolgenx.anilib.common.data.field.BaseSourceField

abstract class SearchField : BaseSourceField<Any>() {
    abstract val type: Int
    var query: String? = null

    init {
        perPage = 22
    }
}