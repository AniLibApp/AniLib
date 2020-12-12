package com.revolgenx.anilib.infrastructure.source.discover

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.common.infrastruture.source.BaseRecyclerSource
import com.revolgenx.anilib.data.field.media.MediaField
import com.revolgenx.anilib.data.model.home.SelectableCommonMediaModel
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.infrastructure.service.media.MediaService
import io.reactivex.disposables.CompositeDisposable

class DiscoverMediaSource(
    private val mediaService: MediaService,
    field: MediaField,
    private val compositeDisposable: CompositeDisposable
) : BaseRecyclerSource<SelectableCommonMediaModel, MediaField>(field) {

    override fun areItemsTheSame(
        first: SelectableCommonMediaModel,
        second: SelectableCommonMediaModel
    ): Boolean =
        first.mediaId == second.mediaId

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