package com.revolgenx.anilib.field.search

import com.revolgenx.anilib.StudioSearchQuery
import com.revolgenx.anilib.constant.AdvanceSearchTypes

class StudioSearchField : BaseAdvanceSearchField() {
    override val type: Int = AdvanceSearchTypes.STUDIO.ordinal

    override fun toQueryOrMutation(): Any {
        return StudioSearchQuery.builder()
            .page(page)
            .perPage(perPage)
            .search(query)
            .build()
    }
}