package com.revolgenx.anilib.common.data.field


abstract class BaseSourceField<T> : BaseField<T>() {
    var page: Int = 0
    open var perPage: Int = 20
}