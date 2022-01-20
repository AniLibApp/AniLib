package com.revolgenx.anilib.entry.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AdvancedScoreModel(var scoreType: String, var score: Double):Parcelable