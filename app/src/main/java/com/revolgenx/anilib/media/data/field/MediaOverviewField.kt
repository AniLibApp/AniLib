package com.revolgenx.anilib.media.data.field

import com.revolgenx.anilib.MediaOverViewQuery
import com.revolgenx.anilib.common.data.field.BaseField
import com.revolgenx.anilib.media.ui.model.MediaModel

class MediaOverviewField(var mediaId: Int = -1) : BaseField<MediaOverViewQuery>() {
    override fun toQueryOrMutation(): MediaOverViewQuery {
        return MediaOverViewQuery(mediaId = nn(mediaId))
    }
}

