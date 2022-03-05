package com.revolgenx.anilib.list.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.extensions.MainSource
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.list.data.model.MediaListModel

class MediaListCollectionSource(val resource: Resource<List<MediaListModel>>) : MainSource<MediaListModel>() {

    override fun areItemsTheSame(first: MediaListModel, second: MediaListModel): Boolean {
        return first.id == second.id
    }

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        if (page.isFirstPage()) {
            when (resource) {
                is Resource.Error -> {
                    postResult(page, Exception(resource.exception))
                }
                is Resource.Loading -> {}
                is Resource.Success -> {
                    postResult(page, resource.data ?: emptyList())
                }
            }
        }
    }

}