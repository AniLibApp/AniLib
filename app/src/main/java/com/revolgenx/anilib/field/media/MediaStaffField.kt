package com.revolgenx.anilib.field.media

import com.revolgenx.anilib.MediaStaffQuery
import com.revolgenx.anilib.field.BaseField
import com.revolgenx.anilib.field.BaseField.Companion.PER_PAGE

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