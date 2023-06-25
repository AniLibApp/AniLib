package com.revolgenx.anilib.studio.ui.model

import com.revolgenx.anilib.StudioMediaQuery
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.fragment.Studio
import com.revolgenx.anilib.media.ui.model.MediaConnectionModel
import com.revolgenx.anilib.media.ui.model.toModel

data class StudioModel(
    val id: Int = -1,
    val name: String? = null,
    val favourites: Int? = null,
    val isFavourite: Boolean = false,
    val siteUrl: String? = null,
    val isAnimationStudio: Boolean = false,
    val media: MediaConnectionModel? = null,
) : BaseModel


fun StudioMediaQuery.Studio.toModel(): StudioModel {
    return StudioModel(
        id = id,
        name = name,
        favourites = favourites,
        isFavourite = isFavourite,
        siteUrl = siteUrl,
    )
}

fun Studio.toModel() = StudioModel(
    id = id,
    name = name,
    media = media?.let { media ->
        MediaConnectionModel(
            nodes = media.nodes?.mapNotNull {
                it?.takeIf { /*if (field.canShowAdult) true else it.mediaContent.isAdult == false todo filter 18 media*/ true }?.media?.toModel()
            }
        )
    }
)