package com.revolgenx.anilib.social.ui.model

import com.revolgenx.anilib.common.ui.model.BaseModel

data class LikeableUnionModel(
    val id: Int,
    val isLiked: Boolean,
    val likeCount: Int
): BaseModel