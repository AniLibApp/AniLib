package com.revolgenx.anilib.review.data.field

import com.revolgenx.anilib.AllReviewQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField
import com.revolgenx.anilib.type.ReviewSort

class AllReviewField : BaseSourceField<AllReviewQuery>() {
    var reviewSort: Int = ReviewSort.ID_DESC.ordinal
    override fun toQueryOrMutation(): AllReviewQuery {
        return AllReviewQuery(
            page = nn(page),
            perPage = nn(perPage),
            sort = nn(listOf(ReviewSort.values()[reviewSort])))
    }
}
