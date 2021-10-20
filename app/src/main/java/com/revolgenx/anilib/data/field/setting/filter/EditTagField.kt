package com.revolgenx.anilib.data.field.setting.filter

import android.os.Parcelable
import com.revolgenx.anilib.data.field.TagState
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditTagField(var isSelected:Boolean, var isExcluded:Boolean, var tag:String):Parcelable