package com.revolgenx.anilib.field

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TagChooserField(var header: String, var tags: List<TagField>) :
    Parcelable
