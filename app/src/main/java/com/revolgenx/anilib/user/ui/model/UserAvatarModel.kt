package com.revolgenx.anilib.user.ui.model

import com.revolgenx.anilib.common.ui.model.BaseImageModel
import com.revolgenx.anilib.fragment.UserAvatar

data class UserAvatarModel(val medium: String?, val large: String?) : BaseImageModel(medium, large)

fun UserAvatar.toModel() = UserAvatarModel(medium, large)