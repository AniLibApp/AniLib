package com.revolgenx.anilib.user.data.field

import com.revolgenx.anilib.common.data.field.BaseField

abstract class BaseUserField<T>(): BaseField<T>(){
    var userId:Int? = null
    var userName:String? = null
}
