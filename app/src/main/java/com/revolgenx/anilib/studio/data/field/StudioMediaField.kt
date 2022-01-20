package com.revolgenx.anilib.studio.data.field

import com.revolgenx.anilib.StudioMediaQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField

class StudioMediaField : BaseSourceField<StudioMediaQuery>() {
    var studioId: Int? = null
    override fun toQueryOrMutation(): StudioMediaQuery {
        return StudioMediaQuery.builder()
            .studioId(studioId)
            .page(page)
            .perPage(perPage)
            .build()
    }
}
