package com.revolgenx.anilib.media.data.model

import com.revolgenx.anilib.common.preference.titlePref

data class MediaTitleModel(
    val english: String? = null,
    val romaji: String? = null,
    val native: String? = null,
    val userPreferred: String? = null
) {
    fun title() = when (titlePref()) {
        "0" -> romaji
        "1" -> english ?: romaji
        "2" -> native ?: romaji
        "3" -> userPreferred ?: romaji
        else -> romaji
    }
}