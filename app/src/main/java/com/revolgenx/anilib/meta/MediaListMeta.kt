package com.revolgenx.anilib.meta

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MediaListMeta(var userId: Int?, var userName: String?,var type:Int = 0) : Parcelable