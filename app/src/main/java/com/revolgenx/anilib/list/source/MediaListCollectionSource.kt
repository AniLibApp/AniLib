package com.revolgenx.anilib.list.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.extensions.MainSource
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.list.data.model.MediaListModel

class MediaListCollectionSource(private val resource: Resource<List<MediaListModel>>) : MainSource<MediaListModel>() {

    override fun areItemsTheSame(first: MediaListModel, second: MediaListModel): Boolean {
        return first.id == second.id
    }

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        if (page.isFirstPage()) {
            when (resource.status) {
                Status.SUCCESS -> {
                    postResult(page, resource.data ?: emptyList())
                }
                Status.ERROR -> {
                    postResult(page, Exception(resource.exception))
                }
                else -> {}
            }
        }
    }

}