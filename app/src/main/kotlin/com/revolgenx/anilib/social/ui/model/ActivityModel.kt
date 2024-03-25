package com.revolgenx.anilib.social.ui.model

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.type.ActivityType
import com.revolgenx.anilib.user.ui.model.UserModel

sealed class ActivityModel: BaseModel {
    abstract val id: Int
    abstract val userId:Int?
    abstract val type: ActivityType
    abstract val replyCount: Int
    abstract val siteUrl: String?
    abstract val createdAt: Int
    abstract val createdAtPrettyTime: String
    abstract val user: UserModel?
    abstract val replies: List<ActivityReplyModel>?
    abstract val likes: List<UserModel>?
    abstract val isSubscribed: MutableState<Boolean>
    abstract val likeCount: MutableIntState
    abstract val isLiked:MutableState<Boolean>
    abstract val isPinned:Boolean
    abstract val isDeleted: MutableState<Boolean>
}