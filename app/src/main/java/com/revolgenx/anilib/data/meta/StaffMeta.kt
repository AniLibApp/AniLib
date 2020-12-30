package com.revolgenx.anilib.data.meta

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StaffMeta(var staffId: Int, var staffUrl: String?):Parcelable
