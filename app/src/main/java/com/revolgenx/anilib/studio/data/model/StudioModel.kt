package com.revolgenx.anilib.studio.data.model

import com.revolgenx.anilib.common.data.model.BaseModel
import com.revolgenx.anilib.media.data.model.MediaConnectionModel

open class StudioModel : BaseModel() {
    var studioName: String? = null
    var favourites: Int? = null
    var isFavourite = false
    var siteUrl: String? = null
    var isAnimationStudio = false
    var media:MediaConnectionModel? = null
}
