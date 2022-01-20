package com.revolgenx.anilib.constant

import com.revolgenx.anilib.type.ReviewRating

enum class AlReviewRating(val value:String) {
    NO_VOTE("NO_VOTE"),

    UP_VOTE("UP_VOTE"),

    DOWN_VOTE("DOWN_VOTE"),

    /**
     * Auto generated constant for unknown enum values
     */
    UNKNOWN("UNKNOWN");
}

fun ReviewRating?.toRating() = this?.let {AlReviewRating.valueOf(rawValue())}