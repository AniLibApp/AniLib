package com.revolgenx.anilib.constant

import com.revolgenx.anilib.type.MediaStatus

enum class AlMediaStatus(val value: String) {
    /**
     * Has completed and is no longer being released
     */
    FINISHED("FINISHED"),

    /**
     * Currently releasing
     */
    RELEASING("RELEASING"),

    /**
     * To be released at a later date
     */
    NOT_YET_RELEASED("NOT_YET_RELEASED"),

    /**
     * Ended before the work could be finished
     */
    CANCELLED("CANCELLED"),

    /**
     * Version 2 only. Is currently paused from releasing and will resume at a later date
     */
    HIATUS("HIATUS"),

    /**
     * Auto generated constant for unknown enum values
     */
    UNKNOWN("UNKNOWN");
}

fun MediaStatus?.toStatus() = this?.let { AlMediaStatus.valueOf(rawValue()) }
