package com.revolgenx.anilib.data.meta

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AiringFilterMeta(
    var notYetAired: Boolean,
    var showFromWatching: Boolean,
    var showFromPlanning: Boolean,
    var sort:Int?
) : Parcelable