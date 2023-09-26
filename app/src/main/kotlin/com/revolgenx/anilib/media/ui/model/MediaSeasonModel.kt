package com.revolgenx.anilib.media.ui.model

import androidx.annotation.StringRes
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcFall
import com.revolgenx.anilib.common.ui.icons.appicon.IcSpring
import com.revolgenx.anilib.common.ui.icons.appicon.IcSummer
import com.revolgenx.anilib.common.ui.icons.appicon.IcWinter
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

fun MediaSeason?.toImageVector() = when (this) {
    MediaSeason.WINTER -> AppIcons.IcWinter
    MediaSeason.SPRING -> AppIcons.IcSpring
    MediaSeason.SUMMER -> AppIcons.IcSummer
    MediaSeason.FALL -> AppIcons.IcFall
    else -> AppIcons.IcWinter
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
