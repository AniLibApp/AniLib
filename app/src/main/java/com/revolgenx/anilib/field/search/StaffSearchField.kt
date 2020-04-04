package com.revolgenx.anilib.field.search

import com.revolgenx.anilib.StaffSearchQuery
import com.revolgenx.anilib.constant.SearchTypes

class StaffSearchField : BrowseField() {

    override val type: Int =SearchTypes.STAFF.ordinal
    override fun toQueryOrMutation(): Any {
        return StaffSearchQuery.builder()
            .page(page)
            .perPage(perPage)
            .search(query)
            .build()
    }

}
