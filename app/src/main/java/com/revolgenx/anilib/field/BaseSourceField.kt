package com.revolgenx.anilib.field

import com.revolgenx.anilib.field.BaseField.Companion.PER_PAGE

abstract class BaseSourceField<T>() : BaseField<T> {
    var page: Int = 1
    var perPage: Int = PER_PAGE
}
