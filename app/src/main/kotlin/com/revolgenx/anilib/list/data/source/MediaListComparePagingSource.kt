package com.revolgenx.anilib.list.data.source

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.source.BasePagingSource
import com.revolgenx.anilib.list.data.field.MediaListCompareField
import com.revolgenx.anilib.list.data.service.MediaListService
import com.revolgenx.anilib.list.ui.model.MediaListModel
import kotlinx.coroutines.flow.single

class MediaListComparePagingSource(
    field: MediaListCompareField,
    private val service: MediaListService
): BasePagingSource<MediaListModel, MediaListCompareField>(field = field) {
    override suspend fun loadPage(): PageModel<MediaListModel> {
        return service.getMediaListCompare(field).single()
    }
}