package com.revolgenx.anilib.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.field.CharacterField
import com.revolgenx.anilib.field.ToggleFavouriteField
import com.revolgenx.anilib.model.character.CharacterModel
import com.revolgenx.anilib.repository.util.Resource
import com.revolgenx.anilib.service.CharacterService
import com.revolgenx.anilib.service.ToggleService
import io.reactivex.disposables.CompositeDisposable

class CharacterViewModel(
    private val characterService: CharacterService,
    private val toggleService: ToggleService
) :
    ViewModel() {

    val characterInfoLiveData by lazy {
        MediatorLiveData<Resource<CharacterModel>>().also {
            it.addSource(characterService.characterInfoLiveData) { res ->
                it.value = res
            }
        }
    }

    val toggleCharacterFavLiveData by lazy {
        MediatorLiveData<Resource<Boolean>>().also {
            it.addSource(toggleService.toggleFavMutableLiveData) { res ->
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

    fun toggleCharacterFav(field: ToggleFavouriteField) {
        toggleCharacterFavLiveData.value = Resource.loading(null)
        toggleService.toggleFavourite(field, compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
