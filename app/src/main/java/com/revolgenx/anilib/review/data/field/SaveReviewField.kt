package com.revolgenx.anilib.review.data.field

import com.revolgenx.anilib.SaveReviewMutation
import com.revolgenx.anilib.common.data.field.BaseField

class SaveReviewField : BaseField<SaveReviewMutation>() {
    var id: Int? = null
    var mediaId: Int? = null
    var summary: String? = null
    var body: String? = null
    var private: Boolean = false
    var score: Int? = 50

    override fun toQueryOrMutation(): SaveReviewMutation {
        return SaveReviewMutation(
            reviewId = nn(id),
            mediaId = nn(mediaId),
            body = nn(body),
            summary = nn(summary),
            score = nn(score),
            private_ = nn(private)
        )
    }
}