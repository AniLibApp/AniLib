package com.revolgenx.anilib.ui.viewmodel.review

import com.revolgenx.anilib.data.field.review.AllReviewField
import com.revolgenx.anilib.infrastructure.service.review.ReviewService
import com.revolgenx.anilib.infrastructure.source.home.discover.AllReviewSource
import com.revolgenx.anilib.type.ReviewSort
import com.revolgenx.anilib.ui.viewmodel.SourceViewModel

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
