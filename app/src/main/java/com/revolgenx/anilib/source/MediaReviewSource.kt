package com.revolgenx.anilib.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.field.overview.MediaReviewField
import com.revolgenx.anilib.model.MediaReviewModel
import com.revolgenx.anilib.repository.util.Status
import com.revolgenx.anilib.service.MediaBrowseService
import io.reactivex.disposables.CompositeDisposable

class MediaReviewSource(
    private val field: MediaReviewField,
    private val browseService: MediaBrowseService,
    private val compositeDisposable: CompositeDisposable
) : BaseRecyclerSource<MediaReviewModel>() {
    override fun areItemsTheSame(first: MediaReviewModel, second: MediaReviewModel): Boolean {
        return first.reviewId == second.reviewId
    }

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        browseService.getMediaReview(field, compositeDisposable) { res ->
            when (res.status) {
                Status.SUCCESS -> {
                    postResult(page, res.data ?: emptyList())
                }
                Status.ERROR -> {
                    postResult(page, Exception(res.exception))
                }
            }
        }
    }
}
