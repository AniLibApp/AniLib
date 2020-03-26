package com.revolgenx.anilib.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.field.studio.StudioMediaField
import com.revolgenx.anilib.model.studio.StudioMediaModel
import com.revolgenx.anilib.service.studio.StudioService
import io.reactivex.disposables.CompositeDisposable

class StudioMediaSource(
    field: StudioMediaField,
    private val studioService: StudioService,
    private val compositeDisposable: CompositeDisposable
) :
    BaseRecyclerSource<StudioMediaModel, StudioMediaField>(field) {
    override fun areItemsTheSame(first: StudioMediaModel, second: StudioMediaModel): Boolean {
        return first.mediaId == second.mediaId
    }

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        studioService.getStudioMedia(field, compositeDisposable) {
            postResult(page, it)
        }
    }
}
