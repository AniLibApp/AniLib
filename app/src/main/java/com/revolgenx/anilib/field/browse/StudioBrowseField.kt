package com.revolgenx.anilib.field.browse

import com.revolgenx.anilib.StudioSearchQuery
import com.revolgenx.anilib.constant.BrowseTypes

class StudioBrowseField : BrowseField() {
    init {
        perPage = 10
    }

    override val type: Int = BrowseTypes.STUDIO.ordinal

    override fun toQueryOrMutation(): Any {
        return StudioSearchQuery.builder()
            .page(page)
            .perPage(perPage)
            .search(query)
            .build()
    }
}