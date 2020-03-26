package com.revolgenx.anilib.field.character

import com.revolgenx.anilib.CharacterMediaQuery
import com.revolgenx.anilib.field.BaseField
import com.revolgenx.anilib.field.BaseField.Companion.PER_PAGE
import com.revolgenx.anilib.field.BaseSourceField

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
