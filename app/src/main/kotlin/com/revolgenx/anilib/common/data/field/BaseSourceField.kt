package com.revolgenx.anilib.common.data.field


abstract class BaseSourceField<T> : BaseField<T>() {
    var page: Int = 1
    open var perPage: Int = 20
}