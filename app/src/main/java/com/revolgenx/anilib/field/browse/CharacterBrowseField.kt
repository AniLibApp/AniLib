package com.revolgenx.anilib.field.browse

import com.revolgenx.anilib.CharacterSearchQuery
import com.revolgenx.anilib.constant.BrowseTypes

class CharacterBrowseField : BrowseField() {
    override val type: Int = BrowseTypes.CHARACTER.ordinal

    override fun toQueryOrMutation(): Any {
        return CharacterSearchQuery.builder()
            .page(page)
            .perPage(perPage)
            .search(query)
            .build()
    }

}