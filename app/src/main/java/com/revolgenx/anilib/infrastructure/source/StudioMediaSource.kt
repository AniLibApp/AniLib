package com.revolgenx.anilib.infrastructure.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.common.source.BaseRecyclerSource
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.studio.data.field.StudioMediaField
import com.revolgenx.anilib.studio.service.StudioService
import io.reactivex.disposables.CompositeDisposable

class StudioMediaSource(
    field: StudioMediaField,
    private val studioService: StudioService,
    private val compositeDisposable: CompositeDisposable
) :
    BaseRecyclerSource<MediaModel, StudioMediaField>(field) {
    override fun areItemsTheSame(first: MediaModel, second: MediaModel): Boolean {
        return first.id == second.id
    }

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        studioService.getStudioMedia(field, compositeDisposable) {
            postResult(page, it)
        }
    }
}
