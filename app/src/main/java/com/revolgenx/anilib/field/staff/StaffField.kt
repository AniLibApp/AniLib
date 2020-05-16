package com.revolgenx.anilib.field.staff

import com.revolgenx.anilib.StaffQuery
import com.revolgenx.anilib.field.BaseField

class StaffField : BaseField<StaffQuery>() {
    var staffId: Int = -1
    override fun toQueryOrMutation(): StaffQuery {
        return StaffQuery.builder()
            .id(staffId)
            .build()
    }
}
