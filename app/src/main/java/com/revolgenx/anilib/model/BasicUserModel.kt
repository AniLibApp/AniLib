package com.revolgenx.anilib.model

open class BasicUserModel() : BaseUserModel() {
    var avatar: UserAvatarImageModel? = null
    var bannerImage: String? = null
    var scoreFormat: Int? = null
}