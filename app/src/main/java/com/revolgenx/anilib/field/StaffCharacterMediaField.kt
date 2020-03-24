package com.revolgenx.anilib.field

import com.revolgenx.anilib.StaffCharacterMediaQuery

class StaffCharacterMediaField : BaseRecyclerField<StaffCharacterMediaQuery>() {
    var staffId: Int? = null
    override fun toQueryOrMutation(): StaffCharacterMediaQuery {
        return StaffCharacterMediaQuery.builder()
            .staffId(staffId)
            .page(page)
            .perPage(perPage)
            .build()
    }
}
