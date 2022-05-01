package com.revolgenx.anilib.staff.data.field

import com.revolgenx.anilib.StaffMediaRoleQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField
import com.revolgenx.anilib.staff.data.constant.StaffMediaRoleSort
import com.revolgenx.anilib.type.MediaSort

class StaffMediaRoleField : BaseSourceField<StaffMediaRoleQuery>() {
    var staffId: Int? = null
    var onList = false
    var sort = StaffMediaRoleSort.START_DATE_DESC

    override fun toQueryOrMutation(): StaffMediaRoleQuery {
        return StaffMediaRoleQuery(
            staffId = nn(staffId),
            page = nn(page),
            perPage = nn(perPage),
            onList = nnBool(onList),
            sort = nn(listOf(MediaSort.TYPE, MediaSort.safeValueOf(sort.name)))
        )
    }
}
