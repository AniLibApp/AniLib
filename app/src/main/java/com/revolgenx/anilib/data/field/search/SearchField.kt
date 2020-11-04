package com.revolgenx.anilib.data.field.search

import com.revolgenx.anilib.data.field.BaseSourceField

abstract class SearchField : BaseSourceField<Any>() {
    abstract val type: Int
    var query: String? = null

    init {
        perPage = 22
    }
}