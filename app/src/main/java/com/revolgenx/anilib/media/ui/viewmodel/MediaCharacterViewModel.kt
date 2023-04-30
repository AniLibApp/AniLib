package com.revolgenx.anilib.media.ui.viewmodel

import com.revolgenx.anilib.character.ui.model.CharacterEdgeModel
import com.revolgenx.anilib.common.ui.screen.PagingViewModel
import com.revolgenx.anilib.media.data.field.MediaCharacterField
import com.revolgenx.anilib.media.data.service.MediaService
import com.revolgenx.anilib.media.data.source.MediaCharacterPagingSource

class MediaCharacterViewModel(private val mediaService: MediaService) :
    PagingViewModel<CharacterEdgeModel,MediaCharacterField, MediaCharacterPagingSource>() {

    override val field = MediaCharacterField()

    override val pagingSource: MediaCharacterPagingSource
        get() = MediaCharacterPagingSource(this.field, mediaService)
}