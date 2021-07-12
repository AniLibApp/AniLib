package com.revolgenx.anilib.data.model.media_info

import com.revolgenx.anilib.data.model.BaseModel
import com.revolgenx.anilib.data.model.user.UserMediaListModel

class MediaSocialFollowingModel : BaseModel() {
    var status: Int? = null
    var score: Double? = null
    var progress: Int? = null
    var user: UserMediaListModel? = null
    var type:Int? = null
}