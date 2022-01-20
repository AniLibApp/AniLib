package com.revolgenx.anilib.character.data.field

import com.revolgenx.anilib.CharacterMediaQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField

class CharacterMediaField :
    BaseSourceField<CharacterMediaQuery>() {
    var characterId: Int? = null
    override fun toQueryOrMutation(): CharacterMediaQuery {
        return CharacterMediaQuery.builder()
            .characterId(characterId)
            .page(page)
            .perPage(perPage)
            .build()
    }
}
