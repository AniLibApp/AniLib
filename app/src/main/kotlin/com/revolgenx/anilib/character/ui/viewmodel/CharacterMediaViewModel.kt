package com.revolgenx.anilib.character.ui.viewmodel

import com.revolgenx.anilib.character.data.field.CharacterMediaField
import com.revolgenx.anilib.character.data.service.CharacterService
import com.revolgenx.anilib.character.data.source.CharacterMediaSource
import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.media.ui.model.MediaModel

class CharacterMediaViewModel(private val characterService: CharacterService) :
    PagingViewModel<MediaModel, CharacterMediaField, CharacterMediaSource>() {
    override var field: CharacterMediaField = CharacterMediaField()

    override val pagingSource: CharacterMediaSource
        get() = CharacterMediaSource(this.field, characterService)

}