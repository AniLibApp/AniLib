package com.revolgenx.anilib.ui.viewmodel.home.discover

import com.revolgenx.anilib.data.field.review.AllReviewField
import com.revolgenx.anilib.infrastructure.service.review.ReviewService
import com.revolgenx.anilib.infrastructure.source.home.discover.AllReviewSource
import com.revolgenx.anilib.ui.viewmodel.SourceViewModel


class DiscoverReviewViewModel(private val reviewService: ReviewService) :
    SourceViewModel<AllReviewSource, AllReviewField>() {
    override var field: AllReviewField = AllReviewField()

    override fun createSource(): AllReviewSource {
        source = AllReviewSource(field, reviewService, compositeDisposable)
        return source!!
    }

}
