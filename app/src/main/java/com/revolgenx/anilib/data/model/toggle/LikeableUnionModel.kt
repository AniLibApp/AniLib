package com.revolgenx.anilib.data.model.toggle

import com.revolgenx.anilib.data.model.BaseModel

class LikeableUnionModel : BaseModel() {
    var isLiked: Boolean = false
    var likeCount:Int = 0
}