package com.revolgenx.anilib.meta

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ViewPagerContainerMeta<T : Parcelable>(
    var containerType: ViewPagerContainerType,
    var data: T
) : Parcelable

enum class ViewPagerContainerType {
    CHARACTER, STAFF
}