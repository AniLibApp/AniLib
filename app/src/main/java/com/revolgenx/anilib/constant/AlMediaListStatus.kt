package com.revolgenx.anilib.constant

import com.revolgenx.anilib.type.MediaListStatus

enum class AlMediaListStatus(val status: String) {
    CURRENT("CURRENT"),
    PLANNING("PLANNING"),
    COMPLETED("COMPLETED"),
    DROPPED("DROPPED"),
    PAUSED("PAUSED"),
    REPEATING("REPEATING"),
    UNKNOWN("UNKNOWN")
}

fun MediaListStatus?.toListStatus() = this?.let {AlMediaListStatus.valueOf(rawValue())}