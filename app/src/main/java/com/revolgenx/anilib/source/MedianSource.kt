package com.revolgenx.anilib.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.extensions.MainSource
import com.revolgenx.anilib.constant.PAGE_SIZE
import com.revolgenx.anilib.field.SeasonField
import com.revolgenx.anilib.model.CommonMediaModel
import com.revolgenx.anilib.model.entry.MediaEntryListModel
import com.revolgenx.anilib.service.media.MediaService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class MedianSource(
    private val mediaService: MediaService,
    seasonField: SeasonField,
    private val compositeDisposable: CompositeDisposable
) : BaseRecyclerSource<CommonMediaModel, SeasonField>(seasonField) {

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