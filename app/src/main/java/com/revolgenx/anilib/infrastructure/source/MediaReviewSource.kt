package com.revolgenx.anilib.infrastructure.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.common.source.BaseRecyclerSource
import com.revolgenx.anilib.media.data.field.MediaReviewField
import com.revolgenx.anilib.media.data.model.MediaReviewModel
import com.revolgenx.anilib.media.service.MediaInfoService
import io.reactivex.disposables.CompositeDisposable

class MediaReviewSource(
    field: MediaReviewField,
    private val browseService: MediaInfoService,
    private val compositeDisposable: CompositeDisposable
) : BaseRecyclerSource<MediaReviewModel, MediaReviewField>(field) {
    override fun areItemsTheSame(first: MediaReviewModel, second: MediaReviewModel): Boolean {
        return first.id == second.id
    }

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        browseService.getMediaReview(field, compositeDisposable) { res ->
           postResult(page, res)
        }
    }
}
