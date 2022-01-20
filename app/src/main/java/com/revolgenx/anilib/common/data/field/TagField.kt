package com.revolgenx.anilib.common.data.field

import android.os.Parcelable
import com.revolgenx.anilib.common.data.meta.TagState
import kotlinx.parcelize.Parcelize

@Parcelize
data class TagField(var tag:String ,var tagState: TagState):Parcelable

