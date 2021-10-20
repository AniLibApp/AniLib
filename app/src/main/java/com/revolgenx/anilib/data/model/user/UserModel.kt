package com.revolgenx.anilib.data.model.user

import com.revolgenx.anilib.data.model.BaseModel

open class UserModel : BaseModel() {
    var name: String? = null
    var avatar: AvatarModel? = null
    var unreadNotificationCount:Int? = null
    var isFollowing = false
    var isFollower = false
    var isBlocked = false
    val isMutual get() = isFollower == isFollowing
}