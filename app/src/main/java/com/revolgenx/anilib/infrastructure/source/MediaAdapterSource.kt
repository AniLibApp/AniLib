package com.revolgenx.anilib.infrastructure.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.common.infrastruture.source.BaseRecyclerSource
import com.revolgenx.anilib.media.data.field.MediaField
import com.revolgenx.anilib.common.data.model.CommonMediaModel
import com.revolgenx.anilib.infrastructure.service.media.MediaService
import com.revolgenx.anilib.media.data.model.MediaModel
import io.reactivex.disposables.CompositeDisposable

class MediaAdapterSource(
    private val mediaService: MediaService,
    field: MediaField,
    private val compositeDisposable: CompositeDisposable
) : BaseRecyclerSource<CommonMediaModel, MediaField>(field) {

    val resources = mutableMapOf<Int, MediaModel>()
    override fun areItemsTheSame(first: CommonMediaModel, second: CommonMediaModel): Boolean =
        first.mediaId == second.mediaId

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        mediaService.getMedia(field, compositeDisposable) {
            it.data?.forEach { resources[it.id] = it }
            postResult(page, it)
        }
    }

    override fun onPageClosed(page: Page) {
        resources.clear()
        super.onPageClosed(page)
    }

}