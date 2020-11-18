package com.revolgenx.anilib.data.meta

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ListEditorMeta(
    var mediaId: Int?,
    var type: Int?,
    var title: String?,
    var coverImage: String?,
    var bannerImage: String?
) : Parcelable