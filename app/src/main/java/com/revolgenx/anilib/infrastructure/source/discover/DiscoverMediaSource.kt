package com.revolgenx.anilib.infrastructure.source.discover

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.common.infrastruture.source.BaseRecyclerSource
import com.revolgenx.anilib.media.data.field.MediaField
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.infrastructure.service.media.MediaService
import com.revolgenx.anilib.media.data.model.MediaModel
import io.reactivex.disposables.CompositeDisposable

class DiscoverMediaSource(
    private val mediaService: MediaService,
    field: MediaField,
    private val compositeDisposable: CompositeDisposable
) : BaseRecyclerSource<MediaModel, MediaField>(field) {

    override fun areItemsTheSame(
        first: MediaModel,
        second: MediaModel
    ): Boolean = first.id == second.id

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        mediaService.getSelectableMedia(field, compositeDisposable) {
            if (it.status == Status.SUCCESS) {
                it.data?.firstOrNull()?.isSelected = true
            }
            postResult(page, it)
        }
    }

}