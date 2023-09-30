package com.revolgenx.anilib.character.ui.viewmodel

import android.text.Spanned
import androidx.compose.runtime.mutableStateOf
import com.revolgenx.anilib.character.data.field.CharacterField
import com.revolgenx.anilib.character.data.service.CharacterService
import com.revolgenx.anilib.character.ui.model.CharacterModel
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.social.factory.markdown
import kotlinx.coroutines.flow.Flow

class CharacterAboutViewModel(private val characterService: CharacterService) :
    ResourceViewModel<CharacterModel, CharacterField>() {
    override val field: CharacterField = CharacterField()
    override fun load(): Flow<CharacterModel?> = characterService.getCharacter(field)

}