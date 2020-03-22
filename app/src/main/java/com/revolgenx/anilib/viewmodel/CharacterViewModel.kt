package com.revolgenx.anilib.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.field.CharacterField
import com.revolgenx.anilib.model.character.CharacterModel
import com.revolgenx.anilib.repository.util.Resource
import com.revolgenx.anilib.service.CharacterService
import com.revolgenx.anilib.service.ToggleService
import io.reactivex.disposables.CompositeDisposable

class CharacterViewModel(private val characterService: CharacterService) :
    ViewModel() {

    val characterInfoLiveData by lazy {
        MediatorLiveData<Resource<CharacterModel>>().also {
            it.addSource(characterService.characterInfoLiveData) { res ->
                it.value = res
            }
        }
    }

    private val compositeDisposable by lazy {
        CompositeDisposable()
    }

    fun getCharacterInfo(field: CharacterField) {
        characterInfoLiveData.value = Resource.loading(null)
        characterService.getCharacterInfo(field, compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
