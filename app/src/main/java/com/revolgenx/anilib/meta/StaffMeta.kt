package com.revolgenx.anilib.meta

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StaffMeta(var staffId: Int, var staffUrl: String?):Parcelable
