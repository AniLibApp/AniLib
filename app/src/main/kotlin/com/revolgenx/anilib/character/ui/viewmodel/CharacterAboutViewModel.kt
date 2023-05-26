package com.revolgenx.anilib.character.ui.viewmodel

import com.revolgenx.anilib.character.data.field.CharacterField
import com.revolgenx.anilib.character.data.service.CharacterService
import com.revolgenx.anilib.character.ui.model.CharacterModel
import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import kotlinx.coroutines.flow.Flow

class CharacterAboutViewModel(private val characterService: CharacterService) :
    ResourceViewModel<CharacterModel, CharacterField>() {
    override val field: CharacterField = CharacterField()
    override fun loadData(): Flow<CharacterModel?> = characterService.getCharacter(field)
}