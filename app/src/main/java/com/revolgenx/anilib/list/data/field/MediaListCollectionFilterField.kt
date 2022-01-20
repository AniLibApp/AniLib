package com.revolgenx.anilib.list.data.field

class MediaListCollectionFilterField {
    var search: String = ""
    var formatsIn = mutableListOf<Int>()
    var status: Int? = null
    var genre: String? = null
    var sort: Int? = null
}