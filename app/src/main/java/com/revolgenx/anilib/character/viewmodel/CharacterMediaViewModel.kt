package com.revolgenx.anilib.character.viewmodel

import com.revolgenx.anilib.character.data.field.CharacterMediaField
import com.revolgenx.anilib.character.service.CharacterService
import com.revolgenx.anilib.infrastructure.source.CharacterMediaSource
import com.revolgenx.anilib.common.viewmodel.SourceViewModel

class CharacterMediaViewModel(private val characterService: CharacterService) :
    SourceViewModel<CharacterMediaSource, CharacterMediaField>() {
    override var field: CharacterMediaField = CharacterMediaField()
    override fun createSource(): CharacterMediaSource {
        source = CharacterMediaSource(field, characterService, compositeDisposable)
        return source!!
    }
}