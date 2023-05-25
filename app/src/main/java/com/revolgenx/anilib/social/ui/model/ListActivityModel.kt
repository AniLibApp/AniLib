package com.revolgenx.anilib.social.ui.model

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
    override val isSubscribed: Boolean = false,
    override val likeCount: Int = 0,
    override val isLiked: Boolean = false,
    override val isPinned: Boolean = false,
    override val createdAt: Int = 0,
    override val createdAtPrettyTime: String = "",
    override val user: UserModel? = null,
    override val replies: List<ActivityReplyModel>? = null,
    override val likes: List<UserModel>? = null,
    var status: String? = null,
    var progress: String? = null,
    var media: MediaModel? = null
) : ActivityModel, BaseModel(id) {
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
        likeCount = likeCount,
        replyCount = replyCount,
        isSubscribed = isSubscribed ?: false,
        type = type!!,
        user = user?.activityUser?.toModel(),
        likes = likes?.mapNotNull {
            it?.likeUsers?.toModel()
        },
        siteUrl = siteUrl,
        createdAt = createdAt,
        createdAtPrettyTime = createdAt.toLong().prettyTime(),
        userId = userId,
        isLiked = isLiked ?: false
    )
}
