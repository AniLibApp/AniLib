package com.revolgenx.anilib.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.field.list.MediaListField
import com.revolgenx.anilib.model.list.MediaListModel
import com.revolgenx.anilib.repository.util.Resource
import com.revolgenx.anilib.repository.util.Status
import com.revolgenx.anilib.service.list.MediaListService
import io.reactivex.disposables.CompositeDisposable

class MediaListSource(
    field: MediaListField,
    private val list: MutableList<MediaListModel>,
    private val mediaListService: MediaListService,
    private val compositeDisposable: CompositeDisposable
) : BaseRecyclerSource<MediaListModel, MediaListField>(field) {
    override fun areItemsTheSame(first: MediaListModel, second: MediaListModel): Boolean {
        return first.baseId == second.baseId
    }

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        if (page.isFirstPage()) {
            if (list.isEmpty()) {
                mediaListService.getMediaList(field, compositeDisposable) {
                    postResult(page, it)

                    if (it.status == Status.SUCCESS) {
                        list.addAll(it.data as List<MediaListModel>)
                    }
                }
            } else {
                postResult(page, Resource.success(list))
            }
        } else {
            postResult(page, emptyList<MediaListModel>())
        }
    }
}
