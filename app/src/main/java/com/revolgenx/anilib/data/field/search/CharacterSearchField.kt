package com.revolgenx.anilib.data.field.search

import com.revolgenx.anilib.CharacterSearchQuery
import com.revolgenx.anilib.constant.SearchTypes

class CharacterSearchField : SearchField() {
    override val type: Int = SearchTypes.CHARACTER.ordinal

    override fun toQueryOrMutation(): Any {
        return CharacterSearchQuery.builder()
            .page(page)
            .perPage(perPage)
            .search(query)
            .build()
    }

}