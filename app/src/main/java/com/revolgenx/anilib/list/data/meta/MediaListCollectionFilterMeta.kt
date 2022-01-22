package com.revolgenx.anilib.list.data.meta

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaListCollectionFilterMeta(
    var search: String = "",
    var formatsIn: MutableList<Int>? = null,
    var status: Int? = null,
    var genre: String? = null,
    var sort: Int? = null,
    var type:Int? = null
) : Parcelable