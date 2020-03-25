package com.revolgenx.anilib.event.meta

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StaffMeta(var staffId: Int, var staffUrl: String?):Parcelable
