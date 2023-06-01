package com.revolgenx.anilib.review.ui.viewmodel

import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.review.data.field.ReviewListField
import com.revolgenx.anilib.review.data.service.ReviewService
import com.revolgenx.anilib.review.data.source.ReviewListSource
import com.revolgenx.anilib.review.ui.model.ReviewModel

class ReviewListViewModel(
    private val service: ReviewService
) : PagingViewModel<ReviewModel, ReviewListField, ReviewListSource>() {

    override val field: ReviewListField = ReviewListField()
    override val pagingSource: ReviewListSource
        get() = ReviewListSource(this.field, service)
}