package com.revolgenx.anilib.media.data.field

import com.revolgenx.anilib.MediaStaffQuery
import com.revolgenx.anilib.common.data.field.BaseField

class MediaStaffField : BaseField<MediaStaffQuery>() {
    var mediaId = -1
    var page = 0
    var perPage = PER_PAGE

    override fun toQueryOrMutation(): MediaStaffQuery {
        return MediaStaffQuery(page = nn(page), perPage = nn(perPage), mediaId = nn(mediaId))
    }

}