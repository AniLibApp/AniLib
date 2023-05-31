package com.revolgenx.anilib.character.data.field

import com.revolgenx.anilib.CharacterActorQuery
import com.revolgenx.anilib.common.data.field.BaseField

data class CharacterActorField(
    var characterId: Int? = null
) : BaseField<CharacterActorQuery>() {
    override fun toQueryOrMutation(): CharacterActorQuery {
        return CharacterActorQuery(
            characterId = nn(characterId),
        )
    }
}