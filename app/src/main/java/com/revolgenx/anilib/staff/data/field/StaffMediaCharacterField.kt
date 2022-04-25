package com.revolgenx.anilib.staff.data.field

import com.revolgenx.anilib.StaffCharacterMediaQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField
import com.revolgenx.anilib.staff.data.constant.StaffMediaCharacterSort
import com.revolgenx.anilib.type.MediaSort

class StaffMediaCharacterField : BaseSourceField<StaffCharacterMediaQuery>() {
    var staffId: Int? = null
    var onList = false
    var sort = StaffMediaCharacterSort.START_DATE_DESC

    override fun toQueryOrMutation(): StaffCharacterMediaQuery {
        return StaffCharacterMediaQuery(
            staffId =  nn(staffId),
            page = nn(page),
            perPage = nn(perPage),
            onList = nnBool(onList),
            sort = nn(listOf(MediaSort.safeValueOf(sort.name)))
        )
    }
}
