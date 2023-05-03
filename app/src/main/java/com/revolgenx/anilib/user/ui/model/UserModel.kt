package com.revolgenx.anilib.user.ui.model

import com.revolgenx.anilib.UserQuery
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.user.ui.model.stats.UserGenreStatisticModel
import com.revolgenx.anilib.user.ui.model.stats.UserStatisticTypesModel
import com.revolgenx.anilib.user.ui.model.stats.UserStatisticsModel
import com.revolgenx.anilib.user.ui.model.stats.toModel

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

    var following: Int = 0,
    var followers: Int = 0,
) : BaseModel(id) {
    val isMutual get() = isFollower && isFollowing
}


fun UserQuery.User.toModel(): UserModel {
    val avatar = avatar?.userAvatar?.toModel()
    return UserModel(
        id = id,
        name = name,
        avatar = avatar,
        bannerImage = bannerImage ?: avatar?.image,
        isBlocked = isBlocked ?: false,
        isFollower = isFollower ?: false,
        isFollowing = isFollowing ?: false,
        about = about ?: "",
        siteUrl = siteUrl,
        statistics = statistics?.let { stats ->
            UserStatisticTypesModel(
                anime = stats.anime?.userMediaStatistics?.toModel(),
                manga = stats.manga?.userMediaStatistics?.toModel()
            )
        }
    )
}
