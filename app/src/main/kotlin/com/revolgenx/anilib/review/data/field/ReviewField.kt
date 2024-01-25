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


enum class ReviewListSort(val raw: String){
    NEWEST("ID_DESC"),
    SCORE("SCORE_DESC"),
    RATING("RATING_DESC"),
    CREATED_AT("CREATED_AT_DESC"),
    UPDATED_AT("UPDATED_AT_DESC"),
}

data class ReviewListField(
    var sort: ReviewListSort = ReviewListSort.NEWEST
) : BaseSourceField<ReviewListQuery>() {
    override fun toQueryOrMutation(): ReviewListQuery {
        return ReviewListQuery(
            page = nn(page),
            perPage = nn(perPage),
            sort = nn(listOf(ReviewSort.safeValueOf(sort.raw)))
        )
    }
}