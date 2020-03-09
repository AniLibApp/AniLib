package com.revolgenx.anilib.model.field.overview

import com.revolgenx.anilib.MediaCharacterQuery
import com.revolgenx.anilib.model.field.BaseField
import com.revolgenx.anilib.model.field.BaseField.Companion.PER_PAGE
import com.revolgenx.anilib.type.StaffLanguage

class MediaCharacterField:BaseField<MediaCharacterQuery>{
    var mediaId = -1
    var page = 1
    var perPage = PER_PAGE
    var language = StaffLanguage.JAPANESE.ordinal

    override fun toQuery(): MediaCharacterQuery {
        return MediaCharacterQuery.builder()
            .page(page)
            .perPage(perPage)
            .mediaId(mediaId)
            .language(StaffLanguage.values()[language])
            .build()
    }

}