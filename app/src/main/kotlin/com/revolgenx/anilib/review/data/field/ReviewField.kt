package com.revolgenx.anilib.review.data.field

import com.revolgenx.anilib.ReviewListQuery
import com.revolgenx.anilib.ReviewQuery
import com.revolgenx.anilib.common.data.field.BaseField
import com.revolgenx.anilib.common.data.field.BaseSourceField
import com.revolgenx.anilib.type.ReviewSort

data class ReviewField(var reviewId: Int? = null) : BaseField<ReviewQuery>() {
    override fun toQueryOrMutation(): ReviewQuery {
        return ReviewQuery(reviewId = nn(reviewId))
    }
}

data class ReviewListField(
    var sort: ReviewSort = ReviewSort.ID_DESC
) : BaseSourceField<ReviewListQuery>() {
    override fun toQueryOrMutation(): ReviewListQuery {
        return ReviewListQuery(
            page = nn(page),
            perPage = nn(perPage),
            sort = nn(listOf(sort))
        )
    }
}