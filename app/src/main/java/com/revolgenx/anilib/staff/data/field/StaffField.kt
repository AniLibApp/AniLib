package com.revolgenx.anilib.staff.data.field

import com.revolgenx.anilib.StaffQuery
import com.revolgenx.anilib.common.data.field.BaseField

class StaffField : BaseField<StaffQuery>() {
    var staffId: Int = -1
    override fun toQueryOrMutation(): StaffQuery {
        return StaffQuery.builder()
            .id(staffId)
            .build()
    }
}
