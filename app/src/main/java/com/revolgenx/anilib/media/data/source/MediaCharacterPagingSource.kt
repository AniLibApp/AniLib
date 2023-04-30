package com.revolgenx.anilib.media.data.source

import com.revolgenx.anilib.character.ui.model.CharacterEdgeModel
import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.source.BasePagingSource
import com.revolgenx.anilib.media.data.field.MediaCharacterField
import com.revolgenx.anilib.media.data.service.MediaService
import kotlinx.coroutines.flow.single

class MediaCharacterPagingSource(
    field: MediaCharacterField,
    private val service: MediaService
) : BasePagingSource<CharacterEdgeModel, MediaCharacterField>(field) {

    override suspend fun loadPage(): PageModel<CharacterEdgeModel> {
        return service.getMediaCharacterList(field).single()
    }

}