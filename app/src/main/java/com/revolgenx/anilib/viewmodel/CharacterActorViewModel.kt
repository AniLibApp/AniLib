package com.revolgenx.anilib.viewmodel

import com.revolgenx.anilib.field.CharacterVoiceActorField
import com.revolgenx.anilib.service.CharacterService
import com.revolgenx.anilib.source.CharacterActorSource

class CharacterActorViewModel(private val characterService: CharacterService) :
    SourceViewModel<CharacterActorSource, CharacterVoiceActorField>() {
    override var source: CharacterActorSource? = null

    override fun createSource(field: CharacterVoiceActorField): CharacterActorSource {
        source = CharacterActorSource(field, characterService, compositeDisposable)
        return source!!
    }

}
