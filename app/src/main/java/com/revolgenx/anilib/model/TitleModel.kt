package com.revolgenx.anilib.model

import android.content.Context
import com.revolgenx.anilib.preference.titlePref

class TitleModel {
    var english: String? = null
    var romaji: String? = null
    var native: String? = null
    var userPreferred: String? = null

    fun title(context: Context) = when (context.titlePref()) {
        0 -> english
        1 -> romaji
        2 -> native
        3 -> userPreferred
        else -> ""
    }
}