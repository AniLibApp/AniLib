package com.revolgenx.anilib.social.ui.model

import com.revolgenx.anilib.fragment.ActivityUser
import com.revolgenx.anilib.fragment.LikeUsers
import com.revolgenx.anilib.fragment.MessengerUser
import com.revolgenx.anilib.user.ui.model.UserAvatarModel
import com.revolgenx.anilib.user.ui.model.UserModel


fun LikeUsers.toModel() = UserModel(
    id = id,
    name = name,
    avatar = avatar?.let {
        UserAvatarModel(it.medium, it.large)
    },
    isBlocked = isBlocked ?: false,
    isFollowing = isFollowing ?: false,
    isFollower = isFollower ?: false
)

fun ActivityUser.toModel() = UserModel(
    id = id,
    name = name,
    avatar = avatar?.let {
        UserAvatarModel(it.medium, it.large)
    }
)

fun MessengerUser.toModel() = UserModel(
    id = id,
    name = name,
    avatar = avatar?.let {
        UserAvatarModel(it.medium, it.large)
    }
)