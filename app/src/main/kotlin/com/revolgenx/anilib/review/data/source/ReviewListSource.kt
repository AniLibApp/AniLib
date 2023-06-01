package com.revolgenx.anilib.review.data.source

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.source.BasePagingSource
import com.revolgenx.anilib.review.data.field.ReviewListField
import com.revolgenx.anilib.review.data.service.ReviewService
import com.revolgenx.anilib.review.ui.model.ReviewModel
import kotlinx.coroutines.flow.single

class ReviewListSource(
    field: ReviewListField,
    private val service: ReviewService
) : BasePagingSource<ReviewModel, ReviewListField>(field) {
    override suspend fun loadPage(): PageModel<ReviewModel> {
        return service.getReviewList(field).single()
    }
}
