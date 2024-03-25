package com.revolgenx.anilib.social.ui.model

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.revolgenx.anilib.ActivityUnionQuery
import com.revolgenx.anilib.common.ext.prettyTime
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.toModel
import com.revolgenx.anilib.type.ActivityType
import com.revolgenx.anilib.user.ui.model.UserModel

data class ListActivityModel(
    override val id: Int,
    override val userId: Int? = null,
    override val type: ActivityType = ActivityType.UNKNOWN__,
    override val replyCount: Int = 0,
    override val siteUrl: String? = null,
    override val isPinned: Boolean = false,
    override val createdAt: Int = 0,
    override val createdAtPrettyTime: String = "",
    override val user: UserModel? = null,
    override val replies: List<ActivityReplyModel>? = null,
    override val likes: List<UserModel>? = null,
    override val isLiked: MutableState<Boolean> = mutableStateOf(false),
    override val likeCount: MutableIntState = mutableIntStateOf(0),
    override val isSubscribed: MutableState<Boolean> = mutableStateOf(false),
    override val isDeleted: MutableState<Boolean> = mutableStateOf(false),

    var status: String? = null,
    var progress: String? = null,
    var media: MediaModel? = null
) : ActivityModel() {
    val progressStatus: String
        get() = "${status?.capitalize()}${if (progress.isNullOrBlank()) " " else " $progress of "}${media!!.title!!.userPreferred}"
}


fun ActivityUnionQuery.OnListActivity.toModel(): ListActivityModel {
    return ListActivityModel(
        id = id,
        media = media?.let {
            MediaModel(
                id = it.id,
                title = it.title?.mediaTitle?.toModel(),
                type = it.type,
                coverImage = it.coverImage?.mediaCoverImage?.toModel(),
                bannerImage = it.bannerImage,
                isAdult = it.isAdult == true,
            )
        },
        status = status!!,
        progress = progress ?: "",
        replyCount = replyCount,
        type = type!!,
        user = user?.activityUser?.toModel(),
        likes = likes?.mapNotNull {
            it?.likeUsers?.toModel()
        },
        siteUrl = siteUrl,
        createdAt = createdAt,
        createdAtPrettyTime = createdAt.toLong().prettyTime(),
        userId = userId,
        likeCount = mutableIntStateOf(likeCount),
        isSubscribed = mutableStateOf(isSubscribed ?: false),
        isLiked = mutableStateOf(isLiked ?: false),
    )
}
