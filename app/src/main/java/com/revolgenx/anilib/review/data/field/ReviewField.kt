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
                ReviewQuery.builder()
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
            ReviewFieldType.MUTATE -> {
                SaveReviewMutation.builder()
                    .apply {
                        model?.let {

                            reviewId(it.id)

                            mediaId?.let {
                                mediaId(it)
                            }

                            body(it.body)
                            it.summary?.let {
                                summary(it)
                            }

                            it.score?.let {
                                score(it)
                            }

                            private_(it.private)

                        }
                    }.build()
            }
            ReviewFieldType.DELETE -> {
                DeleteReviewMutation.builder()
                    .reviewId(model?.id)
                    .build()
            }
        }
    }

    enum class ReviewFieldType {
        QUERY, MUTATE, DELETE
    }
}
