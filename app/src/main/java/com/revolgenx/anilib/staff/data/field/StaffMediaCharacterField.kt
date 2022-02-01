package com.revolgenx.anilib.staff.data.field

import com.revolgenx.anilib.StaffCharacterMediaQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField

class StaffMediaCharacterField : BaseSourceField<StaffCharacterMediaQuery>() {
    var staffId: Int? = null
    override fun toQueryOrMutation(): StaffCharacterMediaQuery {
        return StaffCharacterMediaQuery(
            staffId =  nn(staffId),
            page = nn(page),
            perPage = nn(perPage)
        )
    }
}
