package com.revolgenx.anilib.model

class CoverImageModel {
    var medium: String? = null
    var large: String? = null
    var extraLarge: String? = null

    val image: String
        get() = large ?: medium ?: ""

    val largeImage: String
        get() = extraLarge ?: image
}