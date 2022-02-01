package com.revolgenx.anilib.character.data.field

import com.revolgenx.anilib.CharacterQuery
import com.revolgenx.anilib.common.data.field.BaseField

class CharacterField : BaseField<CharacterQuery>() {
    var characterId: Int = -1
    override fun toQueryOrMutation(): CharacterQuery {
        return CharacterQuery(characterId = nn(characterId))
    }
}
