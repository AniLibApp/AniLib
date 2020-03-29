package com.revolgenx.anilib.field

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TagField(var tag:String ,var isTagged:Boolean):Parcelable
