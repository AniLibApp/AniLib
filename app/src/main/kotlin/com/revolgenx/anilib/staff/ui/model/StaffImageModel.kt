package com.revolgenx.anilib.staff.ui.model

import com.revolgenx.anilib.common.ui.model.BaseImageModel
import com.revolgenx.anilib.fragment.StaffImage

data class StaffImageModel(val medium:String?,val large:String?) : BaseImageModel(medium, large)

fun StaffImage.toModel() = StaffImageModel(medium, large)