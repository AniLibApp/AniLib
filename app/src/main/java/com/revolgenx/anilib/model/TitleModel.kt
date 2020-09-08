package com.revolgenx.anilib.model

import android.content.Context
import com.revolgenx.anilib.preference.titlePref

class TitleModel {
    var english: String? = null
    var romaji: String? = null
    var native: String? = null
    var userPreferred: String? = null

    constructor()
    constructor(
        english: String?,
        romaji: String?,
        native: String?,
        userPreferred: String?
    ) {
        this.english = english
        this.romaji = romaji
        this.native = native
        this.userPreferred = userPreferred
    }


    fun title(context: Context) = when (context.titlePref()) {
        "0" -> romaji
        "1" -> english ?: romaji
        "2" -> native ?: romaji
        "3" -> userPreferred ?: romaji
        else -> romaji
    }
}