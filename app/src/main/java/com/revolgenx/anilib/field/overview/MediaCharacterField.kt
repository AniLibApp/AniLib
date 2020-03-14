package com.revolgenx.anilib.field.overview

import com.revolgenx.anilib.MediaCharacterQuery
import com.revolgenx.anilib.field.BaseField
import com.revolgenx.anilib.field.BaseField.Companion.PER_PAGE
import com.revolgenx.anilib.type.StaffLanguage

class MediaCharacterField:BaseField<MediaCharacterQuery>{
    var mediaId = -1
    var page = 1
    var perPage = PER_PAGE
    var language = StaffLanguage.JAPANESE.ordinal

    override fun toQueryOrMutation(): MediaCharacterQuery {
        return MediaCharacterQuery.builder()
            .page(page)
            .perPage(perPage)
            .mediaId(mediaId)
            .language(StaffLanguage.values()[language])
            .build()
    }

}