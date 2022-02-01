package com.revolgenx.anilib.studio.data.field

import com.revolgenx.anilib.StudioMediaQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField

class StudioMediaField : BaseSourceField<StudioMediaQuery>() {
    var studioId: Int? = null
    override fun toQueryOrMutation(): StudioMediaQuery {
        return StudioMediaQuery(studioId = nn(studioId), page = nn(page), perPage = nn(perPage))
    }
}
