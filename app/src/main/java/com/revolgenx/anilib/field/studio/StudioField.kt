package com.revolgenx.anilib.field.studio

import com.revolgenx.anilib.StudioQuery
import com.revolgenx.anilib.field.BaseField

class StudioField : BaseField<StudioQuery>() {
    var studioId: Int? = null
    override fun toQueryOrMutation(): StudioQuery {
        return StudioQuery.builder()
            .studioId(studioId)
            .build()
    }

}
