package com.revolgenx.anilib.infrastructure.service.character

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.data.field.character.CharacterField
import com.revolgenx.anilib.data.field.character.CharacterMediaField
import com.revolgenx.anilib.data.field.character.CharacterVoiceActorField
import com.revolgenx.anilib.data.model.VoiceActorModel
import com.revolgenx.anilib.data.model.character.CharacterMediaModel
import com.revolgenx.anilib.data.model.character.CharacterModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import io.reactivex.disposables.CompositeDisposable

interface CharacterService {
    val characterInfoLiveData:MutableLiveData<Resource<CharacterModel>>
    fun getCharacterInfo(
        field: CharacterField,
        compositeDisposable: CompositeDisposable? = null
    ): LiveData<Resource<CharacterModel>>

    fun getCharacterMediaInfo(
        field: CharacterMediaField,
        compositeDisposable: CompositeDisposable? = null,
        resourceCallback: ((Resource<List<CharacterMediaModel>>) -> Unit)
    )

    fun getCharacterActor(
        field: CharacterVoiceActorField,
        compositeDisposable: CompositeDisposable? = null,
        resourceCallback: ((Resource<List<VoiceActorModel>>) -> Unit)
    )
}
