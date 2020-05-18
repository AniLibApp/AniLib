package com.revolgenx.anilib.viewmodel

import com.revolgenx.anilib.field.review.AllReviewField
import com.revolgenx.anilib.service.review.ReviewService
import com.revolgenx.anilib.source.home.discover.AllReviewSource


class DiscoverReviewViewModel(private val reviewService: ReviewService) :
    SourceViewModel<AllReviewSource, AllReviewField>() {
    override var field: AllReviewField = AllReviewField()

    override fun createSource(): AllReviewSource {
        source = AllReviewSource(field, reviewService, compositeDisposable)
        return source!!
    }

}
