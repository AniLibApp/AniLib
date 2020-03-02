package com.revolgenx.anilib.model.field

interface BaseField<T> {
    fun toQuery(page:Int, perPage:Int):T
}
