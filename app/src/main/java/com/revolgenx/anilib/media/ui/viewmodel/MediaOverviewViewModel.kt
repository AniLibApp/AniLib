package com.revolgenx.anilib.media.ui.viewmodel

import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.media.data.field.MediaOverviewField
import com.revolgenx.anilib.media.data.service.MediaService
import com.revolgenx.anilib.media.ui.model.MediaModel
import kotlinx.coroutines.flow.Flow


class MediaOverviewViewModel(private val mediaService: MediaService) :
    ResourceViewModel<MediaModel, MediaOverviewField>() {
    override val field = MediaOverviewField()


    override fun loadData(): Flow<MediaModel?> {
        return mediaService.getMediaOverview(field)
    }
}