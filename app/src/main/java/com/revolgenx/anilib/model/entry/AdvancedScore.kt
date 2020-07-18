package com.revolgenx.anilib.model.entry

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AdvancedScore(var scoreType: String, var score: Double):Parcelable