package com.revolgenx.anilib.field.search

import com.revolgenx.anilib.CharacterSearchQuery
import com.revolgenx.anilib.constant.AdvanceSearchTypes

class CharacterSearchField : BaseAdvanceSearchField() {
    override val type: Int = AdvanceSearchTypes.CHARACTER.ordinal

    override fun toQueryOrMutation(): Any {
        return CharacterSearchQuery.builder()
            .page(page)
            .perPage(perPage)
            .search(query)
            .build()
    }

}