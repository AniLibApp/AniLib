package com.revolgenx.anilib.field

import android.os.Parcelable
import com.revolgenx.anilib.constant.MediaTagFilterTypes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TagChooserField(var tagType:MediaTagFilterTypes, var tags: List<TagField>) :
    Parcelable
