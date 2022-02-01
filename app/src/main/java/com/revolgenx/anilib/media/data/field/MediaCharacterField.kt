package com.revolgenx.anilib.media.data.field

import com.revolgenx.anilib.MediaCharacterQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField
import com.revolgenx.anilib.type.StaffLanguage

class MediaCharacterField : BaseSourceField<MediaCharacterQuery>() {
    var mediaId: Int? = null
    var language: Int? = null
    var type: Int? = null

    override fun toQueryOrMutation(): MediaCharacterQuery {
        val mLanguage = language?.let {
            StaffLanguage.values()[it]
        }
        return MediaCharacterQuery(
            page = nn(page),
            perPage = nn(perPage),
            mediaId = nn(mediaId),
            language = nn(mLanguage)
        )
    }

}