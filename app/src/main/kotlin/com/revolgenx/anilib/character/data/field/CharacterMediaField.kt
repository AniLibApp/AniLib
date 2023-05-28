package com.revolgenx.anilib.character.data.field

import com.revolgenx.anilib.CharacterMediaQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField
import com.revolgenx.anilib.type.MediaSort

enum class CharacterMediaSort {
    POPULARITY_DESC,
    SCORE_DESC,
    FAVOURITES_DESC,
    START_DATE_DESC,
    START_DATE,
    TITLE_ROMAJI
}
data class CharacterMediaField(
    var characterId: Int? = null,
    var onList: Boolean = false,
    var sort: CharacterMediaSort = CharacterMediaSort.POPULARITY_DESC
) : BaseSourceField<CharacterMediaQuery>() {

    override var perPage: Int = 30
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