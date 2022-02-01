package com.revolgenx.anilib.media.data.model

import android.content.Context
import com.revolgenx.anilib.common.preference.titlePref

data class MediaTitleModel(
    var english: String? = null,
    var romaji: String? = null,
    var native: String? = null,
    var userPreferred: String? = null
) {
    fun title(context: Context) = when (context.titlePref()) {
        "0" -> romaji
        "1" -> english ?: romaji
        "2" -> native ?: romaji
        "3" -> userPreferred ?: romaji
        else -> romaji
    }
}