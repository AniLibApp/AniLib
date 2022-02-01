package com.revolgenx.anilib.home.recommendation.data.model

import com.revolgenx.anilib.common.data.model.BaseModel
import com.revolgenx.anilib.media.data.model.MediaModel

open class RecommendationModel : BaseModel() {
    var rating: Int? = null
    var userRating: Int? = null
    var recommendationFrom: MediaModel? = null
    var recommended: MediaModel? = null
}
