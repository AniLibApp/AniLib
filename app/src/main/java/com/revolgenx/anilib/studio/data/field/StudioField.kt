package com.revolgenx.anilib.studio.data.field

import com.revolgenx.anilib.StudioQuery
import com.revolgenx.anilib.common.data.field.BaseField

class StudioField : BaseField<StudioQuery>() {
    var studioId: Int? = null
    override fun toQueryOrMutation(): StudioQuery {
        return StudioQuery(studioId = nn(studioId))
    }

}
