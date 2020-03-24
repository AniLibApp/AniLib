package com.revolgenx.anilib.field

import com.revolgenx.anilib.StaffQuery

class StaffField : BaseField<StaffQuery> {
    var staffId: Int = -1
    override fun toQueryOrMutation(): StaffQuery {
        return StaffQuery.builder()
            .id(staffId)
            .build()
    }
}
