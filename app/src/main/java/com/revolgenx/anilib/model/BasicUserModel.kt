package com.revolgenx.anilib.model

open class BasicUserModel() : BaseUserModel() {
    var name: String? = null
    var avatar: UserAvatarImageModel? = null
    var bannerImage: String? = null
    var scoreFormat: Int? = null
}