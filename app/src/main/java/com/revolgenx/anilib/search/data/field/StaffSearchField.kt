package com.revolgenx.anilib.search.data.field

import com.revolgenx.anilib.StaffSearchQuery
import com.revolgenx.anilib.constant.SearchTypes

class StaffSearchField : SearchField() {

    override val type: Int = SearchTypes.STAFF.ordinal
    override fun toQueryOrMutation(): Any {
        return StaffSearchQuery(page = nn(page), perPage = nn(perPage), search = nn(query))
    }

}