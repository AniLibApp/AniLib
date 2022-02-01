package com.revolgenx.anilib.search.data.field

import com.revolgenx.anilib.CharacterSearchQuery
import com.revolgenx.anilib.constant.SearchTypes

class CharacterSearchField : SearchField() {
    override val type: Int = SearchTypes.CHARACTER.ordinal

    override fun toQueryOrMutation(): Any {
        return CharacterSearchQuery(page = nn(page), perPage = nn(perPage), search = nn(query))
    }

}