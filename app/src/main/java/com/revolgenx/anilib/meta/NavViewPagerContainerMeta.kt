package com.revolgenx.anilib.meta

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NavViewPagerContainerMeta<T : Parcelable>(
    var containerType: NavViewPagerContainerType,
    var data: T
) : Parcelable

enum class NavViewPagerContainerType {
    ANIME_STATS, MANGA_STATS
}
