package com.revolgenx.anilib.model.field.overview

import com.revolgenx.anilib.MediaStaffQuery
import com.revolgenx.anilib.model.field.BaseField
import com.revolgenx.anilib.model.field.BaseField.Companion.PER_PAGE

class MediaStaffField :BaseField<MediaStaffQuery>{
    var mediaId = -1
    var page = 0
    var perPage = PER_PAGE

    override fun toQuery(): MediaStaffQuery {
        return MediaStaffQuery.builder()
            .page(page)
            .perPage(perPage)
            .mediaId(mediaId)
            .build()
    }

}