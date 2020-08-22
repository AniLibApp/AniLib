package com.revolgenx.anilib.field.search

import com.revolgenx.anilib.field.BaseSourceField

abstract class SearchField : BaseSourceField<Any>() {
    abstract val type: Int
    var query: String? = null

    init {
        perPage = 22
    }
}