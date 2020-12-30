package com.revolgenx.anilib.data.meta

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ViewPagerContainerMeta(
    var containerType: ViewPagerContainerType,
    var data: Parcelable
) : Parcelable

enum class ViewPagerContainerType {
    CHARACTER, STAFF, FAVOURITE
}