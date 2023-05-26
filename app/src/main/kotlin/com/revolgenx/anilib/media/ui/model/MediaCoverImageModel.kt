package com.revolgenx.anilib.media.ui.model

import com.revolgenx.anilib.fragment.MediaCoverImage

enum class MediaCoverImageType(val image: String) {
    MEDIUM("Medium"),
    LARGE("Large"),
    EXTRA_LARGE("ExtraLarge")
}

data class MediaCoverImageModel(
    val medium: String? = null,
    val large: String? = null,
    val extraLarge: String? = null
) {
    var color: String? = null
    val extraLargeImage = extraLarge ?: large ?: medium
    fun image(which: MediaCoverImageType) = when (which) {
        MediaCoverImageType.MEDIUM -> medium ?: large ?: extraLarge
        MediaCoverImageType.LARGE -> large ?: extraLarge ?: medium
        MediaCoverImageType.EXTRA_LARGE -> extraLargeImage
    }
}

fun MediaCoverImage.toModel() = MediaCoverImageModel(medium, large, extraLarge)
