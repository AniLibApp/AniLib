package com.revolgenx.anilib.studio.data.service

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.studio.data.field.StudioField
import com.revolgenx.anilib.studio.ui.model.StudioModel
import kotlinx.coroutines.flow.Flow

interface StudioService {
    fun getStudioMedia(field: StudioField): Flow<PageModel<MediaModel>>
    fun toggleFavorite(studioId: Int): Flow<Boolean>

}