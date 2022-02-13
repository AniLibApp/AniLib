package com.revolgenx.anilib.review.data.field

import com.revolgenx.anilib.ReviewQuery
import com.revolgenx.anilib.common.data.field.BaseField

class ReviewField : BaseField<ReviewQuery>() {
    var reviewId: Int? = null
    var mediaId: Int? = null
    var userId: Int? = null

    override fun toQueryOrMutation(): ReviewQuery {
        return ReviewQuery(
            reviewId = nn(reviewId),
            mediaId = nn(mediaId),
            userId = nn(userId)
        )
    }

}
