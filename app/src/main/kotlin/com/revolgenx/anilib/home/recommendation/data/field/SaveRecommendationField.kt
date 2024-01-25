package com.revolgenx.anilib.home.recommendation.data.field

import com.revolgenx.anilib.SaveRecommendationMutation
import com.revolgenx.anilib.common.data.field.BaseField
import com.revolgenx.anilib.type.RecommendationRating


data class SaveRecommendationField(
    var mediaId: Int? = null,
    var mediaRecommendationId: Int? = null,
    var rating: RecommendationRating = RecommendationRating.NO_RATING
) :
    BaseField<SaveRecommendationMutation>() {

    override fun toQueryOrMutation(): SaveRecommendationMutation {
        return SaveRecommendationMutation(
            mediaId = nn(mediaId),
            mediaRecommendationId = nn(mediaRecommendationId),
            rating = nn(rating)
        )
    }
}