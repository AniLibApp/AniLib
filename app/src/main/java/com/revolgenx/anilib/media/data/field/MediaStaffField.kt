package com.revolgenx.anilib.media.data.field

import com.revolgenx.anilib.MediaStaffQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField

class MediaStaffField : BaseSourceField<MediaStaffQuery>() {
    var mediaId = -1

    override fun toQueryOrMutation(): MediaStaffQuery {
        return MediaStaffQuery(page = nn(page), perPage = nn(perPage), mediaId = nn(mediaId))
    }

}