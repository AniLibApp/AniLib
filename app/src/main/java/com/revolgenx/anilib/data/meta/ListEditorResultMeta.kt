package com.revolgenx.anilib.data.meta

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListEditorResultMeta(
    var mediaId: Int?,
    var progress: Int? = null,
    var status: Int?,
    var deleted: Boolean = false
):Parcelable
