package com.revolgenx.anilib.constant

import com.revolgenx.anilib.type.CharacterRole

enum class AlCharacterRole(val value:String) {
    /**
     * A primary character role in the media
     */
    MAIN("MAIN"),

    /**
     * A supporting character role in the media
     */
    SUPPORTING("SUPPORTING"),

    /**
     * A background character in the media
     */
    BACKGROUND("BACKGROUND"),

    /**
     * Auto generated constant for unknown enum values
     */
    UNKNOWN("UNKNOWN");
}

fun CharacterRole?.toRole() = this?.let {AlCharacterRole.valueOf(rawValue)}