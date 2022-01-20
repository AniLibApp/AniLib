package com.revolgenx.anilib.character.viewmodel

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.character.data.field.CharacterField
import com.revolgenx.anilib.character.data.model.CharacterModel
import com.revolgenx.anilib.common.data.field.ToggleFavouriteField
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.character.service.CharacterService
import com.revolgenx.anilib.common.viewmodel.BaseViewModel
import com.revolgenx.anilib.infrastructure.service.toggle.ToggleService

class CharacterViewModel(
    private val characterService: CharacterService,
    private val toggleService: ToggleService
) :
    BaseViewModel() {

    val field = CharacterField()
    val toggleField = ToggleFavouriteField()

    val characterInfoLiveData = MutableLiveData<Resource<CharacterModel>>()
    val toggleFavouriteLiveData = MutableLiveData<Resource<Boolean>>()

    fun getCharacterInfo(field: CharacterField) {
        characterInfoLiveData.value = Resource.loading(null)
        characterService.getCharacterInfo(field, compositeDisposable){
            characterInfoLiveData.value = it
        }
    }

    fun toggleCharacterFav(field: ToggleFavouriteField) {
        toggleFavouriteLiveData.value = Resource.loading(null)
        toggleService.toggleFavourite(field, compositeDisposable){
            toggleFavouriteLiveData.value = it
        }
    }

}