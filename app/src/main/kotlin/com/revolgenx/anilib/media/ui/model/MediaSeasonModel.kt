package com.revolgenx.anilib.media.ui.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import com.revolgenx.anilib.R
import com.revolgenx.anilib.type.MediaSeason

@StringRes
fun MediaSeason?.toStringRes(): Int {
    return when (this) {
        MediaSeason.WINTER -> R.string.winter
        MediaSeason.SPRING -> R.string.spring
        MediaSeason.SUMMER -> R.string.summer
        MediaSeason.FALL -> R.string.fall
        else -> R.string.unknown
    }
}

@DrawableRes
fun MediaSeason?.toDrawableRes(): Int {
    return when (this) {
        MediaSeason.WINTER -> R.drawable.ic_winter
        MediaSeason.SPRING -> R.drawable.ic_spring
        MediaSeason.SUMMER -> R.drawable.ic_summer
        MediaSeason.FALL -> R.drawable.ic_fall
        else -> R.drawable.ic_winter
    }
}

fun MediaSeason?.nextSeason(): MediaSeason {
    return when (this) {
        MediaSeason.WINTER -> MediaSeason.SPRING
        MediaSeason.SPRING -> MediaSeason.SUMMER
        MediaSeason.SUMMER -> MediaSeason.FALL
        MediaSeason.FALL -> MediaSeason.WINTER
        else -> MediaSeason.WINTER
    }
}

fun MediaSeason?.previousSeason(): MediaSeason {
    return when (this) {
        MediaSeason.WINTER -> MediaSeason.FALL
        MediaSeason.SPRING -> MediaSeason.WINTER
        MediaSeason.SUMMER -> MediaSeason.SPRING
        MediaSeason.FALL -> MediaSeason.SUMMER
        else -> MediaSeason.WINTER
    }
}



fun seasonFromMonth(monthOfYear: Int): MediaSeason {
    monthOfYear.let {
        return when (it) {
            12, 1, 2 -> {
                MediaSeason.WINTER
            }
            3, 4, 5 -> {
                MediaSeason.SPRING
            }
            6, 7, 8 -> {
                MediaSeason.SUMMER
            }
            else -> MediaSeason.FALL
        }
    }
}
