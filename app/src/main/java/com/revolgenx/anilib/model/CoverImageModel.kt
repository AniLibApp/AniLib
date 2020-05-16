package com.revolgenx.anilib.model

import android.content.Context
import com.revolgenx.anilib.preference.imageQuality

class CoverImageModel {
    var medium: String? = null
    var large: String? = null
    var extraLarge: String? = null

    val sImage: String
        get() = medium ?: large ?: ""

    val image: String
        get() = large ?: medium ?: ""

    val largeImage: String
        get() = extraLarge ?: ""//image

    fun image(context: Context) =
        when (context.imageQuality()) {
            0 -> image
            1 -> sImage
            2 -> largeImage
            else ->""
        }

}