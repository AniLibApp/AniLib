package com.revolgenx.anilib.media.ui.viewmodel

import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.media.data.field.MediaStatsField
import com.revolgenx.anilib.media.data.service.MediaService
import com.revolgenx.anilib.media.ui.model.MediaStatsModel
import kotlinx.coroutines.flow.Flow

class MediaStatsViewModel(private val mediaService: MediaService) :
    ResourceViewModel<MediaStatsModel, MediaStatsField>() {

    override val field: MediaStatsField = MediaStatsField()

    override fun load(): Flow<MediaStatsModel?> {
        return mediaService.getMediaStats(field)
    }
}