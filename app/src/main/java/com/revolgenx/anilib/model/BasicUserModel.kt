package com.revolgenx.anilib.model

import com.revolgenx.anilib.BasicUserQuery
import com.revolgenx.anilib.type.ScoreFormat

class BasicUserModel() : BaseUserModel() {
    lateinit var name: String
    lateinit var avatar: UserAvatarImageModel
    lateinit var bannerImage: String
    var scoreFormat: Int = ScoreFormat.`$UNKNOWN`.ordinal

}