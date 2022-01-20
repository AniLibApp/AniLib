package com.revolgenx.anilib.media.data.model

import com.revolgenx.anilib.common.data.model.BaseModel

class MediaTagModel : BaseModel() {
    var name: String = ""
    var description: String? = null
    var category: String? = null
    var isMediaSpoilerTag: Boolean = false
    var rank: Int? = null
    var isAdult: Boolean = false
}
