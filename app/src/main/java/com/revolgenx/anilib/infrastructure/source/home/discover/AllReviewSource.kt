package com.revolgenx.anilib.infrastructure.source.home.discover

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.data.field.review.AllReviewField
import com.revolgenx.anilib.data.model.review.ReviewModel
import com.revolgenx.anilib.infrastructure.service.review.ReviewService
import com.revolgenx.anilib.common.infrastruture.source.BaseRecyclerSource
import io.reactivex.disposables.CompositeDisposable

class AllReviewSource(
    field: AllReviewField
    , private val reviewService: ReviewService
    , private val compositeDisposable: CompositeDisposable
) : BaseRecyclerSource<ReviewModel, AllReviewField>(field) {

    override fun areItemsTheSame(first: ReviewModel, second: ReviewModel): Boolean {
        return first.reviewId == second.reviewId
    }

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        reviewService.getAllReview(field, compositeDisposable) {
            postResult(page, it)
        }
    }
}
