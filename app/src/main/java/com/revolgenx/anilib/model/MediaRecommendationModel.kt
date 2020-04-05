package com.revolgenx.anilib.model

import com.revolgenx.anilib.model.recommendation.RecommendationModel


open class MediaRecommendationModel : RecommendationModel() {
    var mediaId: Int? = null
    var mediaRecommendationId: Int? = null
}
