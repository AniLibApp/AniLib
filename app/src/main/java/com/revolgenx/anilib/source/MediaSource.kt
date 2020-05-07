package com.revolgenx.anilib.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.model.CommonMediaModel
import com.revolgenx.anilib.service.media.MediaService
import io.reactivex.disposables.CompositeDisposable

class MediaSource(
    private val mediaService: MediaService,
    field: com.revolgenx.anilib.field.media.MediaField,
    private val compositeDisposable: CompositeDisposable
) : BaseRecyclerSource<CommonMediaModel, com.revolgenx.anilib.field.media.MediaField>(field) {

    val resources = mutableMapOf<Int, CommonMediaModel>()
    override fun areItemsTheSame(first: CommonMediaModel, second: CommonMediaModel): Boolean =
        first.mediaId == second.mediaId

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        mediaService.getMedia(field, compositeDisposable) {
            it.data?.forEach { resources[it.baseId!!] = it }
            postResult(page, it)
        }
    }

    override fun onPageClosed(page: Page) {
        resources.clear()
        super.onPageClosed(page)
    }

}