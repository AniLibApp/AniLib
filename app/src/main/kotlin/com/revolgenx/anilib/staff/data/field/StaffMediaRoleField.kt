package com.revolgenx.anilib.staff.data.field

import com.revolgenx.anilib.StaffMediaRoleQuery
import com.revolgenx.anilib.common.data.field.BaseSourceUserField
import com.revolgenx.anilib.type.MediaSort

enum class StaffMediaRoleSort {
    POPULARITY_DESC,
    SCORE_DESC,
    FAVOURITES_DESC,
    START_DATE_DESC,
    START_DATE,
    TITLE_ROMAJI
}

data class StaffMediaRoleField(
    var staffId: Int? = null,
    var onList: Boolean = false,
    var sort: StaffMediaRoleSort = StaffMediaRoleSort.START_DATE_DESC
) : BaseSourceUserField<StaffMediaRoleQuery>() {

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