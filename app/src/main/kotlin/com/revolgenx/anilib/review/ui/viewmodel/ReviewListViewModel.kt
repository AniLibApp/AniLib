package com.revolgenx.anilib.review.ui.viewmodel

import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.review.data.field.ReviewListField
import com.revolgenx.anilib.review.data.service.ReviewService
import com.revolgenx.anilib.review.data.source.ReviewListPagingSource
import com.revolgenx.anilib.review.ui.model.ReviewModel

class ReviewListViewModel(
    private val service: ReviewService
) : PagingViewModel<ReviewModel, ReviewListField, ReviewListPagingSource>() {

    override val field: ReviewListField = ReviewListField()
    override val pagingSource: ReviewListPagingSource
        get() = ReviewListPagingSource(this.field, service)
}