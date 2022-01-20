package com.revolgenx.anilib.media.data.model

import android.content.Context
import com.revolgenx.anilib.common.data.model.BaseImageModel
import com.revolgenx.anilib.common.preference.imageQuality

class MediaCoverImageModel :
    BaseImageModel() {
    var color: String? = null
    var extraLarge:String? = null
    val sImage: String
        get() = medium ?: large ?: ""

    val largeImage: String
        get() = extraLarge ?: image

    fun image(context: Context) =
        when (context.imageQuality()) {
            "0" -> image
            "1" -> sImage
            "2" -> largeImage
            else -> image
        }

}