package com.revolgenx.anilib.character.viewmodel

import com.revolgenx.anilib.character.data.field.CharacterVoiceActorField
import com.revolgenx.anilib.character.service.CharacterService
import com.revolgenx.anilib.infrastructure.source.CharacterActorSource
import com.revolgenx.anilib.common.viewmodel.SourceViewModel

class CharacterActorViewModel(private val characterService: CharacterService) :
    SourceViewModel<CharacterActorSource, CharacterVoiceActorField>() {

    override var field: CharacterVoiceActorField = CharacterVoiceActorField()

    override fun createSource(): CharacterActorSource {
        source = CharacterActorSource(field, characterService, compositeDisposable)
        return source!!
    }

}