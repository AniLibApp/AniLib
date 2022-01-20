package com.revolgenx.anilib.infrastructure.source.home.discover

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.review.data.field.AllReviewField
import com.revolgenx.anilib.review.data.model.ReviewModel
import com.revolgenx.anilib.review.service.ReviewService
import com.revolgenx.anilib.common.infrastruture.source.BaseRecyclerSource
import io.reactivex.disposables.CompositeDisposable

class AllReviewSource(
    field: AllReviewField
    , private val reviewService: ReviewService
    , private val compositeDisposable: CompositeDisposable
) : BaseRecyclerSource<ReviewModel, AllReviewField>(field) {

    override fun areItemsTheSame(first: ReviewModel, second: ReviewModel): Boolean {
        return first.id == second.id
    }

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        reviewService.getAllReview(field, compositeDisposable) {
            postResult(page, it)
        }
    }
}
