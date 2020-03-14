package com.revolgenx.anilib.model

import com.revolgenx.anilib.type.RecommendationRating

open class MediaRecommendationModel : BaseMediaModel() {
    var recommendationId: Int? = null
    var mediaRecommendationId: Int? = null
    var rating: Int? = null
    var userRating: Int? = null
    var title: TitleModel? = null
    var averageScore: Int? = null
    var coverImage: CoverImageModel? = null
}
