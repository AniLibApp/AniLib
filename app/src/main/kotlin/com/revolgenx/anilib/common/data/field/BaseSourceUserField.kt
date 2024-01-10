package com.revolgenx.anilib.common.data.field

abstract class BaseSourceUserField<T> : BaseSourceField<T>() {
    open var userId:Int? = null
    var userName:String? = null
}