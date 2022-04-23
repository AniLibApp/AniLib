package com.revolgenx.anilib.studio.data.field

import com.revolgenx.anilib.StudioMediaQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField
import com.revolgenx.anilib.studio.data.constant.StudioSort
import com.revolgenx.anilib.type.MediaSort

class StudioMediaField : BaseSourceField<StudioMediaQuery>() {
    var studioId: Int? = null
    var onList: Boolean = false
    var sort: StudioSort = StudioSort.START_DATE_DESC

    override fun toQueryOrMutation(): StudioMediaQuery {
        return StudioMediaQuery(
            page = nn(page),
            perPage = nn(perPage),
            studioId = nn(studioId),
            onList = nn(onList),
            sort = nn(listOf(MediaSort.safeValueOf(sort.name)))
        )
    }
}
