package com.revolgenx.anilib.character.ui.viewmodel

import com.revolgenx.anilib.character.data.field.CharacterMediaField
import com.revolgenx.anilib.character.data.service.CharacterService
import com.revolgenx.anilib.character.data.source.CharacterMediaPagingSource
import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.media.ui.model.MediaModel

class CharacterMediaViewModel(private val characterService: CharacterService) :
    PagingViewModel<MediaModel, CharacterMediaField, CharacterMediaPagingSource>() {
    override var field: CharacterMediaField = CharacterMediaField()

    override val pagingSource: CharacterMediaPagingSource
        get() = CharacterMediaPagingSource(this.field, characterService)

}