package com.revolgenx.anilib.data.model

abstract class BaseUserModel : BaseModel() {
    var userId: Int? = null
    var userName: String? = null
    var avatar: UserAvatarImageModel? = null
}