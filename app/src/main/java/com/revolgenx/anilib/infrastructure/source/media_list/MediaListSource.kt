package com.revolgenx.anilib.infrastructure.source.media_list

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.data.field.list.MediaListField
import com.revolgenx.anilib.infrastructure.service.list.MediaListService
import com.revolgenx.anilib.common.source.BaseRecyclerSource
import com.revolgenx.anilib.list.data.model.MediaListModel
import io.reactivex.disposables.CompositeDisposable

class MediaListSource(
    field: MediaListField,
    private val mediaListService: MediaListService,
    private val compositeDisposable: CompositeDisposable
) : BaseRecyclerSource<MediaListModel, MediaListField>(field) {
    override fun areItemsTheSame(first: MediaListModel, second: MediaListModel): Boolean {
        return first.id == second.id
    }

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        mediaListService.getMediaList(field, compositeDisposable) {
            postResult(page, it)
        }
    }
}