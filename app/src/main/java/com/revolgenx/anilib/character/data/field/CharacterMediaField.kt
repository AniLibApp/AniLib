package com.revolgenx.anilib.character.data.field

import com.revolgenx.anilib.CharacterMediaQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField

class CharacterMediaField :
    BaseSourceField<CharacterMediaQuery>() {
    var characterId: Int? = null
    override fun toQueryOrMutation(): CharacterMediaQuery {
        return CharacterMediaQuery(characterId = nn(characterId), page = nn(page), perPage = nn(perPage))
    }
}
