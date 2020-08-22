package com.revolgenx.anilib.field.search

import com.revolgenx.anilib.StudioSearchQuery
import com.revolgenx.anilib.constant.SearchTypes

class StudioSearchField : SearchField() {
    init {
        perPage = 10
    }

    override val type: Int = SearchTypes.STUDIO.ordinal

    override fun toQueryOrMutation(): Any {
        return StudioSearchQuery.builder()
            .page(page)
            .perPage(perPage)
            .search(query)
            .build()
    }
}