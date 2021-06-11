package com.revolgenx.anilib.ui.viewmodel.character

import androidx.lifecycle.MediatorLiveData
import com.revolgenx.anilib.data.field.ToggleFavouriteField
import com.revolgenx.anilib.data.field.character.CharacterField
import com.revolgenx.anilib.data.model.character.CharacterModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.service.ToggleFavouriteService
import com.revolgenx.anilib.infrastructure.service.character.CharacterService
import com.revolgenx.anilib.ui.viewmodel.BaseViewModel

class CharacterViewModel(
    private val characterService: CharacterService,
    private val toggleService: ToggleFavouriteService
) :
    BaseViewModel() {

    val field = CharacterField()
    val toggleField = ToggleFavouriteField()

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

    fun getCharacterInfo(field: CharacterField) {
        characterInfoLiveData.value = Resource.loading(null)
        characterService.getCharacterInfo(field, compositeDisposable)
    }

    fun toggleCharacterFav(field: ToggleFavouriteField) {
        toggleFavouriteLiveData.value = Resource.loading(null)
        toggleService.toggleFavourite(field, compositeDisposable)
    }

}
