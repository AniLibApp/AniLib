package com.revolgenx.anilib.data.field

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TagField(var tag:String ,var tagState:TagState):Parcelable

enum class TagState{
    TAGGED, UNTAGGED, EMPTY
}
