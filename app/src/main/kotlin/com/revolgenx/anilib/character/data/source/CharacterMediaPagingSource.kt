package com.revolgenx.anilib.character.data.source

import com.revolgenx.anilib.character.data.field.CharacterMediaField
import com.revolgenx.anilib.character.data.service.CharacterService
import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.source.BasePagingSource
import com.revolgenx.anilib.media.ui.model.MediaModel
import kotlinx.coroutines.flow.single

class CharacterMediaPagingSource(
    field: CharacterMediaField,
    private val characterService: CharacterService
) :
    BasePagingSource<MediaModel, CharacterMediaField>(field) {
    override suspend fun loadPage(): PageModel<MediaModel> {
        return characterService.getCharacterMedia(field).single()
    }
}