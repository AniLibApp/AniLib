package com.revolgenx.anilib.staff.data.field

import com.revolgenx.anilib.StaffMediaRoleQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField

class StaffMediaRoleField : BaseSourceField<StaffMediaRoleQuery>() {
    var staffId: Int? = null

    override fun toQueryOrMutation(): StaffMediaRoleQuery {
        return StaffMediaRoleQuery(
            staffId = nn(staffId),
            page = nn(page),
            perPage = nn(perPage)
        )
    }
}
