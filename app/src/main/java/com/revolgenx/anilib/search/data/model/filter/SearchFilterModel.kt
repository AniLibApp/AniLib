package com.revolgenx.anilib.search.data.model.filter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchFilterModel(
    val genre: String? = null,
    val tag: String? = null,
    val sort: Int? = null
) : Parcelable