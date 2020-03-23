package com.revolgenx.anilib.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.field.CharacterField
import com.revolgenx.anilib.field.CharacterMediaField
import com.revolgenx.anilib.field.CharacterVoiceActorField
import com.revolgenx.anilib.model.VoiceActorModel
import com.revolgenx.anilib.model.character.CharacterMediaModel
import com.revolgenx.anilib.model.character.CharacterModel
import com.revolgenx.anilib.repository.util.Resource
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
