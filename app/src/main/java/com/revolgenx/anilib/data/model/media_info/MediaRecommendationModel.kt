package com.revolgenx.anilib.data.model.media_info

import com.revolgenx.anilib.data.model.recommendation.RecommendationModel


open class MediaRecommendationModel : RecommendationModel() {
    var mediaId: Int? = null
    var mediaRecommendationId: Int? = null
}
