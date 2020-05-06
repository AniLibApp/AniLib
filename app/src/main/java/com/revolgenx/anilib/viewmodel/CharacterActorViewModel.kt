package com.revolgenx.anilib.viewmodel

import com.revolgenx.anilib.field.character.CharacterVoiceActorField
import com.revolgenx.anilib.service.character.CharacterService
import com.revolgenx.anilib.source.CharacterActorSource

class CharacterActorViewModel(private val characterService: CharacterService) :
    SourceViewModel<CharacterActorSource, CharacterVoiceActorField>() {

    override var field: CharacterVoiceActorField = CharacterVoiceActorField()

    override fun createSource(): CharacterActorSource {
        source = CharacterActorSource(field, characterService, compositeDisposable)
        return source!!
    }

}
