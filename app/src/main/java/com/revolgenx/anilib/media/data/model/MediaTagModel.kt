package com.revolgenx.anilib.media.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaTagModel(
    var id: Int? = null,
    var name: String,
    var description: String? = null,
    var category: String? = null,
    var isMediaSpoilerTag: Boolean = false,
    var rank: Int? = null,
    var isAdult: Boolean = false
) : Parcelable
