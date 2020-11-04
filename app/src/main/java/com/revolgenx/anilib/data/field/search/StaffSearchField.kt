package com.revolgenx.anilib.data.field.search

import com.revolgenx.anilib.StaffSearchQuery
import com.revolgenx.anilib.constant.SearchTypes

class StaffSearchField : SearchField() {

    override val type: Int =SearchTypes.STAFF.ordinal
    override fun toQueryOrMutation(): Any {
        return StaffSearchQuery.builder()
            .page(page)
            .perPage(perPage)
            .search(query)
            .build()
    }

}
