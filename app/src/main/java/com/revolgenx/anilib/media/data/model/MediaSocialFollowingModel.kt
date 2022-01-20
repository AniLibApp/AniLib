package com.revolgenx.anilib.media.data.model

import com.revolgenx.anilib.common.data.model.BaseModel
import com.revolgenx.anilib.user.data.model.UserModel

class MediaSocialFollowingModel : BaseModel() {
    var status: Int? = null
    var score: Double? = null
    var progress: Int? = null
    var user: UserModel? = null
    var type:Int? = null
}