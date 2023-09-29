package com.revolgenx.anilib.media.ui.model

import androidx.annotation.IntDef
import com.revolgenx.anilib.fragment.MediaCoverImage

data class MediaCoverImageModel(
    val medium: String?,
    val large: String?,
    val extraLarge: String?
) {
    companion object {
        @Retention(AnnotationRetention.SOURCE)
        @IntDef(type_medium, type_large, type_extra_large)
        annotation class MediaCoverImageType

        const val type_medium = 0
        const val type_large = 1
        const val type_extra_large = 2
    }

    var color: String? = null
    val extraLargeImage = extraLarge ?: large ?: medium
    fun image(@MediaCoverImageType which: Int) = when (which) {
        type_extra_large -> extraLargeImage
        type_large -> large ?: extraLarge ?: medium
        type_medium -> medium ?: large ?: extraLarge
        else -> large ?: extraLarge ?: medium
    }
}

fun MediaCoverImage.toModel() = MediaCoverImageModel(medium, large, extraLarge)
