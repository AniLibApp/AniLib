package com.revolgenx.anilib.user.ui.model

import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.user.ui.model.stats.UserStatisticTypesModel

data class UserModel(
    val id: Int = -1,
    val name: String? = null,
    val about: String? = null,
    val avatar: UserAvatarModel? = null,
    val bannerImage: String? = null,
    val isFollowing: Boolean = false,
    val isFollower: Boolean = false,
    val isBlocked: Boolean = false,
    val options: UserOptionsModel? = null,
    val mediaListOptions: MediaListOptionModel? = null,
    val favourites: FavouritesModel? = null,
    val statistics: UserStatisticTypesModel? = null,
    val unreadNotificationCount: Int? = null,
    val siteUrl: String? = null,

    val following: Int = 0,
    val followers: Int = 0,
) : BaseModel(id) {
    val isMutual get() = isFollower && isFollowing
}