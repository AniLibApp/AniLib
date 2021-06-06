package com.revolgenx.anilib.data.model.studio

import com.revolgenx.anilib.data.model.BaseModel

open class StudioModel : BaseModel() {
    var studioId: Int? = null
        set(value) {
            field = value
            id = value
        }
    var studioName: String? = null
    var favourites: Int? = null
    var isFavourite = false
    var siteUrl: String? = null
    var isAnimationStudio = false
}
