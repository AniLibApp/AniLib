package com.revolgenx.anilib.search.data.field

import com.revolgenx.anilib.StudioSearchQuery
import com.revolgenx.anilib.constant.SearchTypes

class StudioSearchField : SearchField() {
    init {
        perPage = 10
    }

    override val type: Int = SearchTypes.STUDIO.ordinal

    override fun toQueryOrMutation(): Any {
        return StudioSearchQuery(page = nn(page), perPage = nn(perPage), search = nn(query))
    }
}