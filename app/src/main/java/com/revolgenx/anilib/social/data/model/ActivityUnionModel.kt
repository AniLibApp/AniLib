package com.revolgenx.anilib.social.data.model

import com.revolgenx.anilib.data.model.user.FollowUserModel
import com.revolgenx.anilib.type.ActivityType

abstract class ActivityUnionModel {
    var id: Int? = null
    var replyCount: Int = 0
    var likeCount: Int = 0
    var isSubscribed: Boolean = false
    var createdAt: String = ""
    var user: ActivityUserModel? = null
    var likes: List<FollowUserModel>? = null
    var siteUrl: String? = null
    var type: ActivityType = ActivityType.`$UNKNOWN`
}