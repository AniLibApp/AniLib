package com.revolgenx.anilib.field.studio

import com.revolgenx.anilib.StudioMediaQuery
import com.revolgenx.anilib.field.BaseSourceField

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
