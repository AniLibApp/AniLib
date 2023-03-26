package com.revolgenx.anilib.media.data.source

import com.revolgenx.anilib.MediaQuery
import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.source.BasePagingSource
import com.revolgenx.anilib.media.data.model.MediaFilterModel
import com.revolgenx.anilib.media.data.service.MediaService

class MediaPagingSource(private val filter: MediaFilterModel, private val service: MediaService) : BasePagingSource<MediaQuery.Medium>() {
    override suspend fun loadPage(page: Int, perPage: Int): PageModel<MediaQuery.Medium> {
        return service.getMediaList(page, perPage, filter).dataAssertNoErrors.page.let {
            PageModel(
                pageInfo = it.pageInfo.pageInfo,
                data = it.media.filterNotNull()
            )
        }
    }
}