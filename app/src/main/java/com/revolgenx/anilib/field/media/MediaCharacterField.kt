package com.revolgenx.anilib.field.media

import com.revolgenx.anilib.MediaCharacterQuery
import com.revolgenx.anilib.field.BaseSourceField
import com.revolgenx.anilib.type.StaffLanguage

class MediaCharacterField : BaseSourceField<MediaCharacterQuery>() {
    var mediaId: Int? = null
    var language: Int? = null
    var type: Int? = null

    override fun toQueryOrMutation(): MediaCharacterQuery {
        return MediaCharacterQuery.builder()
            .page(page)
            .perPage(perPage)
            .mediaId(mediaId).apply {
                language?.let {
                    language(StaffLanguage.values()[it])
                }
            }
            .build()
    }

}