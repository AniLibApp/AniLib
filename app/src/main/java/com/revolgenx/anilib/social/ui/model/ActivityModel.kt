package com.revolgenx.anilib.social.ui.model

import com.revolgenx.anilib.type.ActivityType
import com.revolgenx.anilib.user.ui.model.UserModel

interface ActivityModel {
    val id: Int
    val userId:Int?
    val type: ActivityType
    val replyCount: Int
    val siteUrl: String?
    val isSubscribed: Boolean
    val likeCount: Int
    val isLiked:Boolean
    val isPinned:Boolean
    val createdAt: Int
    val createdAtPrettyTime: String
    val user: UserModel?
    val replies: List<ActivityReplyModel>?
    val likes: List<UserModel>?
}