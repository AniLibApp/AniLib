package com.revolgenx.anilib.home.recommendation.data.model

import com.revolgenx.anilib.common.data.model.BaseModel
import com.revolgenx.anilib.media.data.model.MediaModel

open class RecommendationModel : BaseModel() {
    var rating: Int? = null

    /**
     * RecommendationRating
     * */
    var userRating: Int? = null

    // recommended from media id
    var recommendedFromId: Int? = null
    var recommendationFrom: MediaModel? = null
        set(value) {
            recommendedFromId = value?.id
            field = value
        }

    //recommended media id
    var recommendedId: Int? = null
    var recommended: MediaModel? = null
        set(value) {
            recommendedId = value?.id
            field = value
        }

    var onDataChanged: (() -> Unit)? = null
}
