package com.revolgenx.anilib.event.meta

import android.os.Parcel
import android.os.Parcelable
import com.revolgenx.anilib.model.entry.MediaEntryListModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ListEditorMeta(
    var mediaId: Int?,
    var type: Int?,
    var title: String?,
    var coverImage: String?,
    var bannerImage: String?
) : Parcelable