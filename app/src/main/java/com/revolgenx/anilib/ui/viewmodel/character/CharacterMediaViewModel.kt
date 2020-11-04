package com.revolgenx.anilib.ui.viewmodel.character

import com.revolgenx.anilib.data.field.character.CharacterMediaField
import com.revolgenx.anilib.infrastructure.service.character.CharacterService
import com.revolgenx.anilib.infrastructure.source.CharacterMediaSource
import com.revolgenx.anilib.ui.viewmodel.SourceViewModel

class CharacterMediaViewModel(private val characterService: CharacterService) :
    SourceViewModel<CharacterMediaSource, CharacterMediaField>() {
    override var field: CharacterMediaField = CharacterMediaField()
    override fun createSource(): CharacterMediaSource {
        source = CharacterMediaSource(field, characterService, compositeDisposable)
        return source!!
    }
}
