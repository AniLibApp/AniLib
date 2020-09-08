package com.revolgenx.anilib.source.media_list

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.field.list.MediaListField
import com.revolgenx.anilib.model.list.MediaListModel
import com.revolgenx.anilib.service.list.MediaListService
import com.revolgenx.anilib.source.BaseRecyclerSource
import io.reactivex.disposables.CompositeDisposable

class MediaListSource(
    field: MediaListField,
    private val mediaListService: MediaListService,
    private val compositeDisposable: CompositeDisposable
) : BaseRecyclerSource<MediaListModel, MediaListField>(field) {
    override fun areItemsTheSame(first: MediaListModel, second: MediaListModel): Boolean {
        return first.mediaListId == second.mediaListId
    }

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        mediaListService.getMediaList(field, compositeDisposable) {
            postResult(page, it)
        }
    }
}