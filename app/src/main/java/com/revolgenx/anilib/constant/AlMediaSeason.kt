package com.revolgenx.anilib.constant

import com.revolgenx.anilib.type.MediaSeason

enum class AlMediaSeason(val value:String) {
    /**
     * Months December to February
     */
    WINTER("WINTER"),

    /**
     * Months March to May
     */
    SPRING("SPRING"),

    /**
     * Months June to August
     */
    SUMMER("SUMMER"),

    /**
     * Months September to November
     */
    FALL("FALL"),

    /**
     * Auto generated constant for unknown enum values
     */
    UNKNOWN("UNKNOWN");

}

fun MediaSeason?.toSeason() = this?.let {AlMediaSeason.valueOf(rawValue())}