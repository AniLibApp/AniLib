package com.revolgenx.anilib.character.data.service

import com.revolgenx.anilib.character.data.field.CharacterActorField
import com.revolgenx.anilib.character.data.field.CharacterField
import com.revolgenx.anilib.character.data.field.CharacterMediaField
import com.revolgenx.anilib.character.ui.model.CharacterModel
import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.staff.ui.model.StaffModel
import kotlinx.coroutines.flow.Flow

interface CharacterService {
    fun getCharacter(field: CharacterField): Flow<CharacterModel?>
    fun getCharacterMedia(field: CharacterMediaField): Flow<PageModel<MediaModel>>
    fun getCharacterActor(field: CharacterActorField): Flow<List<StaffModel>?>
    fun toggleFavorite(characterId: Int): Flow<Boolean>
}