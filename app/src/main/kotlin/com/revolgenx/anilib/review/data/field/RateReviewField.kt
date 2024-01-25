package com.revolgenx.anilib.review.data.field

import com.revolgenx.anilib.RateReviewMutation
import com.revolgenx.anilib.common.data.field.BaseField
import com.revolgenx.anilib.type.ReviewRating

data class RateReviewField(
    var reviewId: Int? = null,
    var userRating: ReviewRating = ReviewRating.NO_VOTE
) : BaseField<RateReviewMutation>() {
    override fun toQueryOrMutation(): RateReviewMutation {
        return RateReviewMutation(
            reviewId = nn(reviewId),
            rating = nn(userRating)
        )
    }
}