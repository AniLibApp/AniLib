package com.revolgenx.anilib.data.meta.type

enum class MediaListStatus(val status:Int) {
    CURRENT(0),
    PLANNING(1),
    COMPLETED(2),
    PAUSED(4),
    DROPPED(3),
    REPEATING(5),
    UNKNOWN(6);
}