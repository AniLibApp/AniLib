package com.revolgenx.anilib.review.viewmodel

import com.revolgenx.anilib.review.service.ReviewService
import com.revolgenx.anilib.infrastructure.source.home.discover.AllReviewSource
import com.revolgenx.anilib.review.data.field.AllReviewField
import com.revolgenx.anilib.type.ReviewSort
import com.revolgenx.anilib.common.viewmodel.SourceViewModel

class AllReviewViewModel(private val reviewService: ReviewService) :
    SourceViewModel<AllReviewSource, AllReviewField>() {
    override var field: AllReviewField = AllReviewField().also {
        it.reviewSort = ReviewSort.ID_DESC.ordinal
    }

    override fun createSource(): AllReviewSource {
        source = AllReviewSource(field, reviewService, compositeDisposable)
        return source!!
    }
}