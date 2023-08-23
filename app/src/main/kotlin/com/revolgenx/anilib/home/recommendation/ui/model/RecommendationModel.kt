package com.revolgenx.anilib.home.recommendation.ui.model

import com.revolgenx.anilib.RecommendationQuery
import com.revolgenx.anilib.common.ext.orZero
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.toModel
import com.revolgenx.anilib.type.RecommendationRating

data class RecommendationModel(
    val id: Int,
    val rating: Int,
    /**
     * RecommendationRating
     * */
    val userRating: RecommendationRating?,
    // The media the recommendation is from
    val media: MediaModel?,
    // The recommended media
    val mediaRecommendation: MediaModel?
) : BaseModel


fun RecommendationQuery.Recommendation.toModel(): RecommendationModel {
    return RecommendationModel(
        id = id,
        rating = rating.orZero(),
        userRating = userRating,
        media = media?.media?.toModel(),
        mediaRecommendation = mediaRecommendation?.media?.toModel()
    )
}
