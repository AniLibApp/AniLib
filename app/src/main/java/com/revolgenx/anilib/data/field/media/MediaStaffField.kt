package com.revolgenx.anilib.data.field.media

import com.revolgenx.anilib.MediaStaffQuery
import com.revolgenx.anilib.data.field.BaseField

class MediaStaffField :BaseField<MediaStaffQuery>(){
    var mediaId = -1
    var page = 0
    var perPage = PER_PAGE

    override fun toQueryOrMutation(): MediaStaffQuery {
        return MediaStaffQuery.builder()
            .page(page)
            .perPage(perPage)
            .mediaId(mediaId)
            .build()
    }

}