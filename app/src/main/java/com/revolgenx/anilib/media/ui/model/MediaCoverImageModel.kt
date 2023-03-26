package com.revolgenx.anilib.media.ui.model

import com.revolgenx.anilib.fragment.MediaCoverImage
import com.revolgenx.anilib.media.data.type.MediaCoverImageType

data class MediaCoverImageModel(
    val medium: String? = null,
    val large: String? = null,
    val extraLarge: String? = null
) {
    var color: String? = null
    val mExtraLarge = extraLarge ?: large ?: medium
    val image
        get() = when (MediaCoverImageType.LARGE) {
            MediaCoverImageType.MEDIUM -> medium ?: large ?: extraLarge
            MediaCoverImageType.LARGE -> large ?: extraLarge ?: medium
            MediaCoverImageType.EXTRA_LARGE -> mExtraLarge
        }
}

fun MediaCoverImage.toModel() = MediaCoverImageModel(medium, large, extraLarge)
