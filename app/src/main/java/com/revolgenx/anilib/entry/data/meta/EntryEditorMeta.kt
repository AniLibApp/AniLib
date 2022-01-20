package com.revolgenx.anilib.entry.data.meta

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EntryEditorMeta(
    var mediaId: Int?,
    var type: Int?,
    var title: String?,
    var coverImage: String?,
    var bannerImage: String?
) : Parcelable