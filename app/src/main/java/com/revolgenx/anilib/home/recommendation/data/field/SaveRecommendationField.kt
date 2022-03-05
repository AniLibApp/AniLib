package com.revolgenx.anilib.home.recommendation.data.field

import com.revolgenx.anilib.SaveRecommendationMutation
import com.revolgenx.anilib.common.data.field.BaseField
import com.revolgenx.anilib.type.RecommendationRating

open class SaveRecommendationField :
    BaseField<SaveRecommendationMutation>() {
    var mediaId: Int? = null
    var mediaRecommendationId: Int? = null
    var rating: Int = RecommendationRating.NO_RATING.ordinal

    override fun toQueryOrMutation(): SaveRecommendationMutation {
        return SaveRecommendationMutation(
            mediaId = nn(mediaId),
            mediaRecommendationId = nn(mediaRecommendationId),
            rating = nn(RecommendationRating.values()[rating])
        )
    }
}
