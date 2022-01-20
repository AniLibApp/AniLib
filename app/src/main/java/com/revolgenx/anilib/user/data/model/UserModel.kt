package com.revolgenx.anilib.user.data.model

import com.revolgenx.anilib.app.setting.data.model.MediaListOptionModel
import com.revolgenx.anilib.app.setting.data.model.UserOptionsModel
import com.revolgenx.anilib.common.data.model.BaseModel

open class UserModel : BaseModel() {
    var name: String? = null
    var avatar: UserAvatarModel? = null
    var bannerImage:String? = null
    var isFollowing = false
    var isFollower = false
    var isBlocked = false
    var options:UserOptionsModel? = null
    var mediaListOptions:MediaListOptionModel? = null
    var favourites: FavouritesModel? = null
    var statistics: UserStatisticTypesModel? = null

    var unreadNotificationCount:Int? = null


    val isMutual get() = isFollower && isFollowing
}