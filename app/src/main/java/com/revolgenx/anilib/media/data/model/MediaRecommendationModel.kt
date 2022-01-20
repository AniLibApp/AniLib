package com.revolgenx.anilib.media.data.model

import com.revolgenx.anilib.home.recommendation.data.model.RecommendationModel


open class MediaRecommendationModel : RecommendationModel() {
    var mediaId: Int? = null
    var mediaRecommendationId: Int? = null
}
