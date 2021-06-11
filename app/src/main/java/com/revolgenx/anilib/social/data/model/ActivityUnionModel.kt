package com.revolgenx.anilib.social.data.model

import com.revolgenx.anilib.data.model.user.UserModel
import com.revolgenx.anilib.social.data.model.reply.ActivityReplyModel
import com.revolgenx.anilib.type.ActivityType

abstract class ActivityUnionModel {
    var id: Int? = null
    var replyCount: Int = 0
    var likeCount: Int = 0
    var isSubscribed: Boolean = false
    var createdAt: String = ""
    var user: UserModel? = null
    var likes: List<UserModel>? = null
    var replies: List<ActivityReplyModel>? = null
    var siteUrl: String? = null
    var type: ActivityType = ActivityType.`$UNKNOWN`
    var userId:Int? = null
    var isLiked:Boolean = false

    var onDataChanged:(() -> Unit)? = null
}