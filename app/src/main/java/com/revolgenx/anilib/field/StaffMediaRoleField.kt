package com.revolgenx.anilib.field

import com.revolgenx.anilib.StaffMediaRoleQuery
import com.revolgenx.anilib.field.BaseField.Companion.PER_PAGE

class StaffMediaRoleField : BaseRecyclerField<StaffMediaRoleQuery>() {
    var staffId: Int? = null

    override fun toQueryOrMutation(): StaffMediaRoleQuery {
        return StaffMediaRoleQuery.builder()
            .staffId(staffId)
            .page(page)
            .perPage(perPage)
            .build()
    }
}
