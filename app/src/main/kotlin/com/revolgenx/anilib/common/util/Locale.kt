package com.revolgenx.anilib.common.util

import androidx.core.os.LocaleListCompat
import java.util.Locale

fun getDisplayName(lang: String?): String {
    if (lang == null) {
        return ""
    }

    val locale = when (lang) {
        "" -> LocaleListCompat.getAdjustedDefault()[0]
        "zh-CN" -> Locale.forLanguageTag("zh-Hans")
        "zh-TW" -> Locale.forLanguageTag("zh-Hant")
        else -> Locale.forLanguageTag(lang)
    }
    return locale!!.getDisplayName(locale).replaceFirstChar { it.uppercase(locale) }
}