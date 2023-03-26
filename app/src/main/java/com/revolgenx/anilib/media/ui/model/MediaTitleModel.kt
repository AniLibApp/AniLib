package com.revolgenx.anilib.media.ui.model

import com.revolgenx.anilib.fragment.MediaTitle
import com.revolgenx.anilib.media.data.type.MediaTitleType

data class MediaTitleModel(
    val english: String? = null,
    val romaji: String? = null,
    val native: String? = null,
    val userPreferred: String? = null
) {
    val title
        get() = when (MediaTitleType.ROMAJI) {
            MediaTitleType.ROMAJI -> romaji
            MediaTitleType.ENGLISH -> english ?: romaji
            MediaTitleType.NATIVE -> native ?: romaji
            MediaTitleType.USER_PREFERRED -> userPreferred ?: romaji
        }
}

fun MediaTitle.toModel() = MediaTitleModel(english, romaji, native, userPreferred)