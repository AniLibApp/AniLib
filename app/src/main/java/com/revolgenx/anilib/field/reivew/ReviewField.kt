package com.revolgenx.anilib.field.reivew

import com.revolgenx.anilib.ReviewQuery
import com.revolgenx.anilib.field.BaseField

class ReviewField : BaseField<ReviewQuery> {
    var reviewId: Int? = null
    var mediaId: Int? = null
    var userId: Int? = null
    override fun toQueryOrMutation(): ReviewQuery {
        return ReviewQuery.builder()
            .apply {
                reviewId?.let {
                    reviewId(it)
                }
                mediaId?.let {
                    mediaId(it)
                }
                userId?.let {
                    userId(it)
                }
            }.build()
    }
}
