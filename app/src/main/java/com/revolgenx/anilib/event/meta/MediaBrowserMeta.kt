package com.revolgenx.anilib.event.meta

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MediaBrowserMeta(
    var mediaId: Int?,
    var type: Int?,
    var title: String?,
    var coverImage: String?,
    var bannerImage: String?
) :
    Parcelable