package com.revolgenx.anilib.data.field

import android.os.Parcelable
import com.revolgenx.anilib.constant.MediaTagFilterTypes
import kotlinx.parcelize.Parcelize

@Parcelize
data class TagChooserField(var tagType:MediaTagFilterTypes, var tags: List<TagField>) :
    Parcelable
