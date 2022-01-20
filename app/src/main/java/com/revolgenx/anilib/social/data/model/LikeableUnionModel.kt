package com.revolgenx.anilib.social.data.model

import com.revolgenx.anilib.common.data.model.BaseModel

class LikeableUnionModel : BaseModel() {
    var isLiked: Boolean = false
    var likeCount:Int = 0
}