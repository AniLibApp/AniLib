package com.revolgenx.anilib.model.studio

import com.revolgenx.anilib.model.BaseModel

open class StudioModel : BaseModel() {
    var studioId: Int? = null
        set(value) {
            field = value
            baseId = value
        }
    var studioName: String? = null
    var favourites: Int? = null
    var isFavourite = false
    var siteUrl: String? = null
    var isAnimationStudio = false
}
