package com.revolgenx.anilib.data.model.user

import com.revolgenx.anilib.data.model.BaseUserModel

class FollowUserModel:BaseUserModel() {
    var isFollowing = false
    var isFollower = false
    var isBlocked = false
}