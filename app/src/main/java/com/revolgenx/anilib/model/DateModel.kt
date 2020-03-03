package com.revolgenx.anilib.model

class DateModel {
    var year: Int? = null
    var month: Int? = null
    var day: Int? = null
    var date: String = ""
    override fun toString(): String {
        return (year?.let { "$it" } ?: "") + (month?.let { "-$it" } ?: "") + (day?.let { "-$it" } ?: "")
    }
}