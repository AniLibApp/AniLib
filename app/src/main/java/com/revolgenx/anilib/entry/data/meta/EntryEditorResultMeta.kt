package com.revolgenx.anilib.entry.data.meta

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EntryEditorResultMeta(
    var mediaId: Int?,
    var progress: Int? = null,
    var status: Int?,
    var deleted: Boolean = false
): Parcelable