package com.revolgenx.anilib.data.meta

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NavViewPagerContainerMeta(
    var containerType: NavViewPagerContainerType,
    var data: Parcelable
) : Parcelable

enum class NavViewPagerContainerType {
    ANIME_STATS, MANGA_STATS
}
