package com.revolgenx.anilib.data.field.user

import com.revolgenx.anilib.data.field.BaseField

abstract class BaseUserField<T>():BaseField<T> (){
    var userId:Int? = null
    var userName:String? = null
}
