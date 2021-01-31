package com.revolgenx.anilib.data.meta

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaListMeta(var userId: Int?, var userName: String? = null, var type: Int = 0):
    Parcelable