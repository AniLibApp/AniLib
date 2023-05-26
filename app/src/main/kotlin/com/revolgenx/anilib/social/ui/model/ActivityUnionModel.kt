package com.revolgenx.anilib.social.ui.model

import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.fragment.ActivityUser
import com.revolgenx.anilib.fragment.LikeUsers
import com.revolgenx.anilib.fragment.MessengerUser
import com.revolgenx.anilib.user.ui.model.UserAvatarModel
import com.revolgenx.anilib.user.ui.model.UserModel

data class ActivityUnionModel(
    val textActivityModel: TextActivityModel? = null,
    val listActivityModel: ListActivityModel? = null,
    val messageActivityModel: MessageActivityModel? = null
) : BaseModel(textActivityModel?.id ?: listActivityModel?.id ?: messageActivityModel?.id ?: -1)



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