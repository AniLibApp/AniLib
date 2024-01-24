package com.revolgenx.anilib.character.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import com.revolgenx.anilib.character.data.field.CharacterField
import com.revolgenx.anilib.character.data.service.CharacterService
import com.revolgenx.anilib.character.ui.model.CharacterModel
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single

class CharacterAboutViewModel(private val characterService: CharacterService) :
    ResourceViewModel<CharacterModel, CharacterField>() {
    override val field: CharacterField = CharacterField()

    val showToggleErrorMsg = mutableStateOf(false)
    override fun load(): Flow<CharacterModel?> = characterService.getCharacter(field)

    fun toggleFavorite() {
        if (field.characterId == -1) return
        val isFavourite = getData()?.isFavourite ?: return
        isFavourite.value = !isFavourite.value

        launch {
            val toggled = characterService.toggleFavorite(field.characterId).single()
            if(!toggled){
                isFavourite.value = !isFavourite.value
                showToggleErrorMsg.value = true
            }
        }
    }
}