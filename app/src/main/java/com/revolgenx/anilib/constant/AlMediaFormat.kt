package com.revolgenx.anilib.constant

import com.revolgenx.anilib.type.MediaFormat

enum class AlMediaFormat(val value:String) {
    TV("TV"),
    TV_SHORT("TV_SHORT"),
    MOVIE("MOVIE"),
    SPECIAL("SPECIAL"),
    OVA("OVA"),
    ONA("ONA"),
    MUSIC("MUSIC"),
    MANGA("MANGA"),
    NOVEL("NOVEL"),
    ONE_SHOT("ONE_SHOT"),
    UNKNOWN("UNKNOWN");
}

fun MediaFormat?.toFormat() = this?.let {AlMediaFormat.valueOf(rawValue())}