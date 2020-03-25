package com.revolgenx.anilib.viewmodel

import com.revolgenx.anilib.field.CharacterMediaField
import com.revolgenx.anilib.service.CharacterService
import com.revolgenx.anilib.source.CharacterMediaSource

class CharacterMediaViewModel(private val characterService: CharacterService) :
    SourceViewModel<CharacterMediaSource, CharacterMediaField>() {
    override fun createSource(field: CharacterMediaField): CharacterMediaSource {
        source = CharacterMediaSource(field, characterService, compositeDisposable)
        return source!!
    }
}
