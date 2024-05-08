package com.revolgenx.anilib.review.data.field

import com.revolgenx.anilib.SaveReviewMutation
import com.revolgenx.anilib.common.data.field.BaseField

data class SaveReviewField(
    var id: Int? = null,
    var mediaId: Int,
    var summary: String,
    var body: String,
    var private: Boolean,
    var score: Int
): BaseField<SaveReviewMutation>() {

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