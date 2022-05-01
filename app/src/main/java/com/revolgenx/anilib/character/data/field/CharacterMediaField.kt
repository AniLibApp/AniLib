package com.revolgenx.anilib.character.data.field

import com.revolgenx.anilib.CharacterMediaQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField
import com.revolgenx.anilib.character.data.constant.CharacterMediaSort
import com.revolgenx.anilib.type.MediaSort

class CharacterMediaField : BaseSourceField<CharacterMediaQuery>() {
    var characterId: Int? = null
    var onList: Boolean = false
    var sort: CharacterMediaSort = CharacterMediaSort.POPULARITY_DESC

    override fun toQueryOrMutation(): CharacterMediaQuery {
        return CharacterMediaQuery(
            characterId = nn(characterId),
            page = nn(page),
            perPage = nn(perPage),
            onList = nnBool(onList),
            sort = nn(listOf(MediaSort.safeValueOf(sort.name)))
        )
    }
}
