package com.revolgenx.anilib.field.search

import com.revolgenx.anilib.StaffSearchQuery
import com.revolgenx.anilib.constant.AdvanceSearchTypes

class StaffSearchField : BaseAdvanceSearchField() {

    override val type: Int =AdvanceSearchTypes.STAFF.ordinal
    override fun toQueryOrMutation(): Any {
        return StaffSearchQuery.builder()
            .page(page)
            .perPage(perPage)
            .search(query)
            .build()
    }

}
