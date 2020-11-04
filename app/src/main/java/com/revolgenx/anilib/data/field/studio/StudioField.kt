package com.revolgenx.anilib.data.field.studio

import com.revolgenx.anilib.StudioQuery
import com.revolgenx.anilib.data.field.BaseField

class StudioField : BaseField<StudioQuery>() {
    var studioId: Int? = null
    override fun toQueryOrMutation(): StudioQuery {
        return StudioQuery.builder()
            .studioId(studioId)
            .build()
    }

}
