package com.revolgenx.anilib.event.meta

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ListEditorMeta(
    var id: Int,
    var title: String,
    var coverImage: String,
    var bannerImage: String?
) : Parcelable