package com.revolgenx.anilib.data.field.staff

import com.revolgenx.anilib.StaffQuery
import com.revolgenx.anilib.data.field.BaseField

class StaffField : BaseField<StaffQuery>() {
    var staffId: Int = -1
    override fun toQueryOrMutation(): StaffQuery {
        return StaffQuery.builder()
            .id(staffId)
            .build()
    }
}
