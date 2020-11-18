package com.revolgenx.anilib.data.field.review

import com.revolgenx.anilib.RateReviewMutation
import com.revolgenx.anilib.data.field.BaseField
import com.revolgenx.anilib.type.ReviewRating

class RateReviewField : BaseField<RateReviewMutation>() {
    var reviewId: Int? = null
    var reviewRating: Int? = null
    override fun toQueryOrMutation(): RateReviewMutation {
        return RateReviewMutation.builder()
            .apply {
                reviewId?.let {
                    reviewId(it)
                }
                reviewRating?.let {
                    rating(ReviewRating.values()[it])
                }
            }.build()
    }
}
