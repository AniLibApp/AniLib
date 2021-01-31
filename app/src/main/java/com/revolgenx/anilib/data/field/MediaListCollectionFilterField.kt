package com.revolgenx.anilib.data.field

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaListCollectionFilterField(
    var search: String? = null,
    var formatsIn: MutableList<Int>? = null,
    var status: Int? = null,
    var genre: String? = null,
    var listSort: Int? = null
) : Parcelable
