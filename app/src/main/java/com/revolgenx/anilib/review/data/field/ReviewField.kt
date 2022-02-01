package com.revolgenx.anilib.review.data.field

import com.revolgenx.anilib.DeleteReviewMutation
import com.revolgenx.anilib.ReviewQuery
import com.revolgenx.anilib.SaveReviewMutation
import com.revolgenx.anilib.common.data.field.BaseField
import com.revolgenx.anilib.review.data.model.ReviewModel

class ReviewField : BaseField<Any>() {
    var reviewId: Int? = null
    var mediaId: Int? = null
    var userId: Int? = null
    var type: ReviewFieldType = ReviewFieldType.QUERY
    var model: ReviewModel? = null

    override fun toQueryOrMutation(): Any {
        return when (type) {
            ReviewFieldType.QUERY -> {
                ReviewQuery(
                    reviewId = nn(reviewId),
                    mediaId = nn(mediaId),
                    userId = nn(userId)
                )
            }
            ReviewFieldType.MUTATE -> {
                SaveReviewMutation(
                    reviewId = nn(model?.id),
                    mediaId = nn(model?.mediaId),
                    body = nn(model?.body),
                    summary = nn(model?.summary),
                    score = nn(model?.score),
                    private_ = nn(model?.private)
                )
            }
            ReviewFieldType.DELETE -> {
                DeleteReviewMutation(
                    reviewId = nn(model?.id)
                )
            }
        }
    }

    enum class ReviewFieldType {
        QUERY, MUTATE, DELETE
    }
}
