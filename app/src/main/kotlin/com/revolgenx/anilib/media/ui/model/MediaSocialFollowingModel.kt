package com.revolgenx.anilib.media.ui.model

import com.revolgenx.anilib.MediaSocialFollowingQuery
import com.revolgenx.anilib.app.App
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcHappy
import com.revolgenx.anilib.common.ui.icons.appicon.IcNeutral
import com.revolgenx.anilib.common.ui.icons.appicon.IcSad
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.user.ui.model.MediaListOptionModel
import com.revolgenx.anilib.user.ui.model.UserAvatarModel
import com.revolgenx.anilib.user.ui.model.UserModel

data class MediaSocialFollowingModel(
    val id: Int,
    val status: MediaListStatus?,
    val score: Double?,
    val user: UserModel?,
    val type: MediaType?
) : BaseModel {
    val point3Icon get()=
        if (score == 3.0) AppIcons.IcHappy else if (score == 2.0) AppIcons.IcNeutral else if (score == 1.0) AppIcons.IcSad else null
}

fun MediaSocialFollowingQuery.MediaList.toModel() = MediaSocialFollowingModel(
    id = id,
    score = score,
    type = media?.type,
    status = status,
    user = user?.let {
        UserModel(
            id = it.id,
            name = it.name,
            avatar = it.avatar?.let { ava ->
                UserAvatarModel(ava.medium, ava.large)
            },
            mediaListOptions = it.mediaListOptions?.let { opt ->
                MediaListOptionModel(
                    scoreFormat = opt.scoreFormat
                )
            }
        )
    }
)
