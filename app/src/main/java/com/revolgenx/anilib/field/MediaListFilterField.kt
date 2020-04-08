package com.revolgenx.anilib.field

class MediaListFilterField {
    var search: String? = null
    var format: Int? = null
    var status: Int? = null
    var genre: String? = null

    fun isNull() = search == null && format == null && status == null && genre == null
}
