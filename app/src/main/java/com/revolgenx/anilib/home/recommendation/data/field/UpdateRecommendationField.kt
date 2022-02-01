package com.revolgenx.anilib.home.recommendation.data.field

import com.revolgenx.anilib.UpdateRecommendationMutation
import com.revolgenx.anilib.common.data.field.BaseField
import com.revolgenx.anilib.type.RecommendationRating

open class UpdateRecommendationField :
    BaseField<UpdateRecommendationMutation>() {
    var mediaId: Int? = null
    var mediaRecommendationId: Int? = null
    var rating: Int = RecommendationRating.NO_RATING.ordinal
    override fun toQueryOrMutation(): UpdateRecommendationMutation {
        return UpdateRecommendationMutation(
            mediaId = nn(mediaId),
            mediaRecommendationId = nn(mediaRecommendationId),
            rating = nn(RecommendationRating.values()[rating])
        )
    }
}
