package com.revolgenx.anilib.model.user

import com.revolgenx.anilib.model.BaseModel
import com.revolgenx.anilib.model.CommonMediaModel

class StatsGenreModel : BaseModel() {
    var count: Int? = null
    var meanScore: Float? = null
    var timeWatched: String? = null
    var genre: String? = null
    var media: List<CommonMediaModel>? = null
}
