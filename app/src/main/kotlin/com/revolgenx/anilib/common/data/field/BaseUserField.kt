package com.revolgenx.anilib.common.data.field

abstract class BaseUserField<T> : BaseField<T>() {
    open var userId: Int? = null
    var userName: String? = null
}