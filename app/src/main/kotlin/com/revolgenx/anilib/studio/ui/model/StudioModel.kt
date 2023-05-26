package com.revolgenx.anilib.studio.ui.model

import com.revolgenx.anilib.StudioMediaQuery
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.media.ui.model.MediaConnectionModel

data class StudioModel(
    val id: Int = -1,
    val name: String? = null,
    val favourites: Int? = null,
    val isFavourite: Boolean = false,
    val siteUrl: String? = null,
    val isAnimationStudio: Boolean = false,
    val media: MediaConnectionModel? = null,
) : BaseModel(id)


fun StudioMediaQuery.Studio.toModel(): StudioModel {
    return StudioModel(
        id = id,
        name = name,
        favourites = favourites,
        isFavourite = isFavourite,
        siteUrl = siteUrl,
    )
}