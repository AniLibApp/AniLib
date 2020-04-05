package com.revolgenx.anilib.field.browse

import com.revolgenx.anilib.StaffSearchQuery
import com.revolgenx.anilib.constant.BrowseTypes

class StaffBrowseField : BrowseField() {

    override val type: Int =BrowseTypes.STAFF.ordinal
    override fun toQueryOrMutation(): Any {
        return StaffSearchQuery.builder()
            .page(page)
            .perPage(perPage)
            .search(query)
            .build()
    }

}
