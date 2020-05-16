package com.revolgenx.anilib.field.user

import com.revolgenx.anilib.field.BaseField

abstract class BaseUserField<T>():BaseField<T> (){
    var userId:Int? = null
    var userName:String? = null
}
