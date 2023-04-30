package com.revolgenx.anilib.common.data.field

abstract class BaseUserField<T> : BaseField<T>() {
    var userId: Int? = null
    var userName: String? = null
}