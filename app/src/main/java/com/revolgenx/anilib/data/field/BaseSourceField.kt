package com.revolgenx.anilib.data.field

abstract class BaseSourceField<T>() : BaseField<T>() {
    var page: Int? = 1
    var perPage: Int = PER_PAGE
}
