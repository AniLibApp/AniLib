package com.revolgenx.anilib.model.entry

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MediaEntryListModel(var progress: Int?, var status: Int?) : Parcelable
