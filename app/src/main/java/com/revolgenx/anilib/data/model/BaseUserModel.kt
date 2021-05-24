package com.revolgenx.anilib.data.model

abstract class BaseUserModel : BaseModel() {
    var userId: Int? = null
        set(value) {
            field = value
            baseId = value
        }
    var userName: String? = null
    var avatar: UserAvatarImageModel? = null
}