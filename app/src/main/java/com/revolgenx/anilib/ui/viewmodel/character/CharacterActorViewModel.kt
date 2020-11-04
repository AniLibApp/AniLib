package com.revolgenx.anilib.ui.viewmodel.character

import com.revolgenx.anilib.data.field.character.CharacterVoiceActorField
import com.revolgenx.anilib.infrastructure.service.character.CharacterService
import com.revolgenx.anilib.infrastructure.source.CharacterActorSource
import com.revolgenx.anilib.ui.viewmodel.SourceViewModel

class CharacterActorViewModel(private val characterService: CharacterService) :
    SourceViewModel<CharacterActorSource, CharacterVoiceActorField>() {

    override var field: CharacterVoiceActorField = CharacterVoiceActorField()

    override fun createSource(): CharacterActorSource {
        source = CharacterActorSource(field, characterService, compositeDisposable)
        return source!!
    }

}
