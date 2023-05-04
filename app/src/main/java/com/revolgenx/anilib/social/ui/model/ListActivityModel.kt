package com.revolgenx.anilib.social.ui.model

import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.media.ui.model.MediaModel
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
    val getProgressStatus: String
        get() = "${status?.capitalize()}${if (progress.isNullOrBlank()) " " else " $progress of "}${media!!.title!!.userPreferred}"
}