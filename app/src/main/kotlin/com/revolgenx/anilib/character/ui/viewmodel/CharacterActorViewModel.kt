package com.revolgenx.anilib.character.ui.viewmodel

import com.revolgenx.anilib.character.data.field.CharacterActorField
import com.revolgenx.anilib.character.data.service.CharacterService
import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.staff.ui.model.StaffModel
import kotlinx.coroutines.flow.Flow

class CharacterActorViewModel(
    private val service: CharacterService
): ResourceViewModel<List<StaffModel>, CharacterActorField>() {
    override val field: CharacterActorField = CharacterActorField()

    override fun loadData(): Flow<List<StaffModel>?> {
        return service.getCharacterActor(field)
    }
}