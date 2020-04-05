package com.revolgenx.anilib.field.recommendation

import com.revolgenx.anilib.UpdateRecommendationMutation
import com.revolgenx.anilib.field.BaseField
import com.revolgenx.anilib.type.RecommendationRating

open class UpdateRecommendationField :
    BaseField<UpdateRecommendationMutation> {
    var mediaId: Int? = null
    var mediaRecommendationId: Int? = null
    var rating: Int = RecommendationRating.NO_RATING.ordinal
    override fun toQueryOrMutation(): UpdateRecommendationMutation {
        return UpdateRecommendationMutation.builder()
            .mediaId(mediaId)
            .mediaRecommendationId(mediaRecommendationId)
            .rating(RecommendationRating.values()[rating])
            .build()
    }
}
