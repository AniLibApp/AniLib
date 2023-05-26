package com.revolgenx.anilib.media.data.source

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.source.BasePagingSource
import com.revolgenx.anilib.media.data.field.MediaField
import com.revolgenx.anilib.media.data.service.MediaService
import com.revolgenx.anilib.media.ui.model.MediaModel
import kotlinx.coroutines.flow.single

class MediaPagingSource(
    field: MediaField,
    private val service: MediaService
) : BasePagingSource<MediaModel, MediaField>(field) {
    override suspend fun loadPage(): PageModel<MediaModel> {
        return service.getMediaList(field).single()
    }
}