package com.revolgenx.anilib.app.setting.data.field

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditTagField(var isSelected:Boolean, var isExcluded:Boolean, var tag:String):Parcelable