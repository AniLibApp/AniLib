package com.revolgenx.anilib.list.data.source

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.source.BasePagingSource
import com.revolgenx.anilib.list.data.field.MediaListField
import com.revolgenx.anilib.list.data.service.MediaListService
import com.revolgenx.anilib.list.ui.model.MediaListModel
import kotlinx.coroutines.flow.single

class MediaListPagingSource(
    field: MediaListField,
    private val service: MediaListService
): BasePagingSource<MediaListModel, MediaListField>(field) {

    override suspend fun loadPage(): PageModel<MediaListModel> {
        return service.getMediaList(field).single()
    }
}