package com.revolgenx.anilib.field

import com.revolgenx.anilib.CharacterQuery

class CharacterField : BaseField<CharacterQuery> {
    var characterId: Int = -1
    override fun toQueryOrMutation(): CharacterQuery {
        return CharacterQuery.builder()
            .characterId(characterId)
            .build()
    }
}
