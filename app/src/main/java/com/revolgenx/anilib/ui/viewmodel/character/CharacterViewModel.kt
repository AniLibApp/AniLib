package com.revolgenx.anilib.ui.viewmodel.character

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.data.field.ToggleFavouriteField
import com.revolgenx.anilib.data.field.character.CharacterField
import com.revolgenx.anilib.data.model.character.CharacterModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.service.ToggleService
import com.revolgenx.anilib.infrastructure.service.character.CharacterService
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

    val toggleFavouriteLiveData by lazy {
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
        toggleFavouriteLiveData.value = Resource.loading(null)
        toggleService.toggleFavourite(field, compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
