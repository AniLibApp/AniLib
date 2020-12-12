package com.revolgenx.anilib.data.model

import android.content.Context
import com.revolgenx.anilib.common.preference.imageQuality

class CoverImageModel {
    var medium: String? = null
    var large: String? = null
    var extraLarge: String? = null

    val sImage: String
        get() = medium ?: large ?: ""

    val image: String
        get() = large ?: medium ?: ""

    val largeImage: String
        get() = extraLarge ?: image

    constructor()
    constructor(medium: String?, large: String?, extraLarge: String?) {
        this.medium = medium
        this.large = large
        this.extraLarge = extraLarge
    }

    fun image(context: Context) =
        when (context.imageQuality()) {
            "0" -> image
            "1" -> sImage
            "2" -> largeImage
            else -> image
        }

}