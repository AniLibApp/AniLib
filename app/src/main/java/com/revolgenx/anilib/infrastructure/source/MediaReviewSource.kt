package com.revolgenx.anilib.infrastructure.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.common.infrastruture.source.BaseRecyclerSource
import com.revolgenx.anilib.data.field.media.MediaReviewField
import com.revolgenx.anilib.data.model.MediaReviewModel
import com.revolgenx.anilib.infrastructure.service.media.MediaBrowseService
import io.reactivex.disposables.CompositeDisposable

class MediaReviewSource(
    field: MediaReviewField,
    private val browseService: MediaBrowseService,
    private val compositeDisposable: CompositeDisposable
) : BaseRecyclerSource<MediaReviewModel, MediaReviewField>(field) {
    override fun areItemsTheSame(first: MediaReviewModel, second: MediaReviewModel): Boolean {
        return first.reviewId == second.reviewId
    }

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        browseService.getMediaReview(field, compositeDisposable) { res ->
           postResult(page, res)
        }
    }
}
