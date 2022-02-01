package com.revolgenx.anilib.constant

import com.revolgenx.anilib.type.MediaType

enum class AlMediaType(val value:String) {
    /**
     * Japanese Anime
     */
    ANIME("ANIME"),

    /**
     * Asian comic
     */
    MANGA("MANGA"),

    /**
     * Auto generated constant for unknown enum values
     */
    UNKNOWN("UNKNOWN");
}

fun MediaType?.toType() = this?.let {AlMediaType.valueOf(rawValue)}
