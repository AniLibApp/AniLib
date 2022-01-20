package com.revolgenx.anilib.common.data.model

open class BaseNameModel {
    var full: String? = null
    var native: String? = null
    var alternative: List<String>? = null
    var first: String? = null
    var last: String? = null
    var middle: String? = null

    //The currently authenticated users preferred name language. Default romaji for non-authenticated
    var userPreferred: String? = null

}
