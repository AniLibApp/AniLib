package com.revolgenx.anilib.home.discover.viewmodel

import com.revolgenx.anilib.review.service.ReviewService
import com.revolgenx.anilib.infrastructure.source.home.discover.AllReviewSource
import com.revolgenx.anilib.review.data.field.AllReviewField
import com.revolgenx.anilib.common.viewmodel.SourceViewModel

class DiscoverReviewViewModel(private val reviewService: ReviewService) :
    SourceViewModel<AllReviewSource, AllReviewField>() {
    override var field: AllReviewField = AllReviewField()

    override fun createSource(): AllReviewSource {
        source = AllReviewSource(field, reviewService, compositeDisposable)
        return source!!
    }

}