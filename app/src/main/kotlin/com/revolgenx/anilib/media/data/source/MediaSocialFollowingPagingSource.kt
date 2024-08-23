package com.revolgenx.anilib.media.data.source

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.source.BasePagingSource
import com.revolgenx.anilib.media.data.field.MediaSocialFollowingField
import com.revolgenx.anilib.media.data.service.MediaService
import com.revolgenx.anilib.media.ui.model.MediaSocialFollowingModel
import kotlinx.coroutines.flow.single

class MediaSocialFollowingPagingSource(
    field: MediaSocialFollowingField,
    private val service: MediaService
): BasePagingSource<MediaSocialFollowingModel, MediaSocialFollowingField>(field) {

    override suspend fun loadPage(): PageModel<MediaSocialFollowingModel> {
        return service.getMediaSocialFollowingList(field).single()
    }
}