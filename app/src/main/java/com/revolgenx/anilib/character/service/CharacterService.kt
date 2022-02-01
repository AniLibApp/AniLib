package com.revolgenx.anilib.character.service

import com.revolgenx.anilib.character.data.field.CharacterField
import com.revolgenx.anilib.character.data.field.CharacterMediaField
import com.revolgenx.anilib.character.data.field.CharacterVoiceActorField
import com.revolgenx.anilib.character.data.model.VoiceActorModel
import com.revolgenx.anilib.character.data.model.CharacterMediaModel
import com.revolgenx.anilib.character.data.model.CharacterModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.media.data.model.MediaModel
import io.reactivex.disposables.CompositeDisposable

interface CharacterService {
    fun getCharacterInfo(
        field: CharacterField,
        compositeDisposable: CompositeDisposable? = null,
        callback: (Resource<CharacterModel>)->Unit
    )

    fun getCharacterMediaInfo(
        field: CharacterMediaField,
        compositeDisposable: CompositeDisposable? = null,
        resourceCallback: ((Resource<List<MediaModel>>) -> Unit)
    )

    fun getCharacterActor(
        field: CharacterVoiceActorField,
        compositeDisposable: CompositeDisposable? = null,
        resourceCallback: ((Resource<List<VoiceActorModel>>) -> Unit)
    )
}
