package com.revolgenx.anilib.review.data.field

import com.revolgenx.anilib.ReviewListQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField
import com.revolgenx.anilib.type.ReviewSort

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