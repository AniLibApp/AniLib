package com.revolgenx.anilib.field.staff

import com.revolgenx.anilib.StaffCharacterMediaQuery
import com.revolgenx.anilib.field.BaseSourceField

class StaffMediaCharacterField : BaseSourceField<StaffCharacterMediaQuery>() {
    var staffId: Int? = null
    override fun toQueryOrMutation(): StaffCharacterMediaQuery {
        return StaffCharacterMediaQuery.builder()
            .staffId(staffId)
            .page(page)
            .perPage(perPage)
            .build()
    }
}
