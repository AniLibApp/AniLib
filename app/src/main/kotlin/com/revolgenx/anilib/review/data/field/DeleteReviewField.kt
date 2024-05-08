package com.revolgenx.anilib.review.data.field

import com.revolgenx.anilib.DeleteReviewMutation
import com.revolgenx.anilib.common.data.field.BaseField

data class DeleteReviewField(val id: Int): BaseField<DeleteReviewMutation>() {
    override fun toQueryOrMutation(): DeleteReviewMutation {
        return DeleteReviewMutation(reviewId = nn(id))
    }
}