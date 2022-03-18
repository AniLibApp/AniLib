package com.revolgenx.anilib.search.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchFilterEventModel(
    val genre: String? = null,
    val tag: String? = null,
    val sort: Int? = null
) : Parcelable