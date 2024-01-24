package com.revolgenx.anilib.studio.ui.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.revolgenx.anilib.StudioMediaQuery
import com.revolgenx.anilib.common.data.field.BaseField
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.fragment.Studio
import com.revolgenx.anilib.media.ui.model.MediaConnectionModel
import com.revolgenx.anilib.media.ui.model.toModel
import com.revolgenx.anilib.studio.data.field.StudioField

data class StudioModel(
    val id: Int = -1,
    val name: String? = null,
    val favourites: Int? = null,
    val isFavourite: MutableState<Boolean> = mutableStateOf(false),
    val siteUrl: String? = null,
    val isAnimationStudio: Boolean = false,
    val media: MediaConnectionModel? = null,
) : BaseModel


fun StudioMediaQuery.Studio.toModel(): StudioModel {
    return StudioModel(
        id = id,
        name = name,
        favourites = favourites,
        isFavourite = mutableStateOf(isFavourite),
        siteUrl = siteUrl,
    )
}

fun Studio.toModel(field: BaseField<*>) = StudioModel(
    id = id,
    name = name,
    media = media?.let { media ->
        MediaConnectionModel(
            nodes = media.nodes?.mapNotNull {
                it?.takeIf { if (field.canShowAdult) true else it.media.isAdult == false }?.media?.toModel()
            }
        )
    }
)