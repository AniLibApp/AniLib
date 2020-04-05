package com.revolgenx.anilib.field.browse

import com.revolgenx.anilib.field.BaseSourceField

abstract class BrowseField : BaseSourceField<Any>() {
    abstract val type: Int
    var query: String? = null

    init {
        perPage = 22
    }
}