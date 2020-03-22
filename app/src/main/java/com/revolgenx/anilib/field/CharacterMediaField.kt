package com.revolgenx.anilib.field

import com.revolgenx.anilib.CharacterMediaQuery
import com.revolgenx.anilib.field.BaseField.Companion.PER_PAGE

class CharacterMediaField : BaseField<CharacterMediaQuery> {
    var characterId: Int = -1
    var page = 1
    var perPage = PER_PAGE
    override fun toQueryOrMutation(): CharacterMediaQuery {
        return CharacterMediaQuery.builder()
            .characterId(characterId)
            .page(page)
            .perPage(perPage)
            .build()
    }
}
