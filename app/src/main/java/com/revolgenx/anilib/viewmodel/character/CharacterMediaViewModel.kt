package com.revolgenx.anilib.viewmodel.character

import com.revolgenx.anilib.field.character.CharacterMediaField
import com.revolgenx.anilib.service.character.CharacterService
import com.revolgenx.anilib.source.CharacterMediaSource
import com.revolgenx.anilib.viewmodel.SourceViewModel

class CharacterMediaViewModel(private val characterService: CharacterService) :
    SourceViewModel<CharacterMediaSource, CharacterMediaField>() {
    override var field: CharacterMediaField = CharacterMediaField()
    override fun createSource(): CharacterMediaSource {
        source = CharacterMediaSource(field, characterService, compositeDisposable)
        return source!!
    }
}
