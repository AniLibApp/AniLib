package com.revolgenx.anilib.character.data.service

import com.revolgenx.anilib.character.data.field.CharacterField
import com.revolgenx.anilib.character.ui.model.CharacterModel
import kotlinx.coroutines.flow.Flow

interface CharacterService {
    fun getCharacter(field: CharacterField): Flow<CharacterModel?>
}