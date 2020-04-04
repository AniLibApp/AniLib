package com.revolgenx.anilib.field.search

import com.revolgenx.anilib.field.BaseSourceField

abstract class BrowseField : BaseSourceField<Any>() {
    abstract val type: Int
    var query: String? = null

    init {
        perPage = 22
    }
}