package com.revolgenx.anilib.data.field.review

import com.revolgenx.anilib.DeleteReviewMutation
import com.revolgenx.anilib.ReviewQuery
import com.revolgenx.anilib.SaveReviewMutation
import com.revolgenx.anilib.data.field.BaseField
import com.revolgenx.anilib.data.model.review.ReviewModel

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

                            it.reviewId?.let {
                                reviewId(it)
                            }

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

                            it.private?.let {
                                private_(it)
                            }

                        }
                    }.build()
            }
            ReviewFieldType.DELETE -> {
                DeleteReviewMutation.builder()
                    .reviewId(model?.reviewId)
                    .build()
            }
        }
    }

    enum class ReviewFieldType {
        QUERY, MUTATE, DELETE
    }
}
