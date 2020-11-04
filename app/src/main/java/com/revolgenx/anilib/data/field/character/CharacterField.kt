package com.revolgenx.anilib.data.field.character

import com.revolgenx.anilib.CharacterQuery
import com.revolgenx.anilib.data.field.BaseField

class CharacterField : BaseField<CharacterQuery>() {
    var characterId: Int = -1
    override fun toQueryOrMutation(): CharacterQuery {
        return CharacterQuery.builder()
            .characterId(characterId)
            .build()
    }
}
