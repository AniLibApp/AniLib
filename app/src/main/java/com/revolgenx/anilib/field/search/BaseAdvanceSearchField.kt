package com.revolgenx.anilib.field.search

import com.revolgenx.anilib.constant.AdvanceSearchTypes
import com.revolgenx.anilib.field.BaseSourceField

abstract class BaseAdvanceSearchField : BaseSourceField<Any>() {
    abstract val type: Int
    var query: String? = null

    init {
        perPage = 22
    }
}