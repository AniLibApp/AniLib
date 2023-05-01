package com.revolgenx.anilib.media.ui.model

import com.revolgenx.anilib.fragment.MediaTitle

enum class MediaTitleType {
    ROMAJI,
    ENGLISH,
    NATIVE,
    USER_PREFERRED
}

data class MediaTitleModel(
    val english: String? = null,
    val romaji: String? = null,
    val native: String? = null,
    val userPreferred: String? = null
)

fun MediaTitleModel.title(which: MediaTitleType) = when (which) {
    MediaTitleType.ROMAJI -> romaji
    MediaTitleType.ENGLISH -> english ?: romaji
    MediaTitleType.NATIVE -> native ?: romaji
    MediaTitleType.USER_PREFERRED -> userPreferred ?: romaji
}

fun MediaTitle.toModel() = MediaTitleModel(english, romaji, native, userPreferred)

