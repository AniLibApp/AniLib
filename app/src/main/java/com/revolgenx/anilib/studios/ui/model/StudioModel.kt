package com.revolgenx.anilib.studios.ui.model

import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.media.ui.model.MediaConnectionModel

data class StudioModel(
    val id: Int = -1,
    val studioName: String? = null,
    val favourites: Int? = null,
    val isFavourite: Boolean = false,
    val siteUrl: String? = null,
    val isAnimationStudio: Boolean = false,
    val media: MediaConnectionModel? = null,
) : BaseModel(id)