package com.revolgenx.anilib.media.data.field

import com.revolgenx.anilib.MediaCharacterQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField
import com.revolgenx.anilib.type.StaffLanguage

data class MediaCharacterField(
    var mediaId: Int? = null,
    var language: StaffLanguage = StaffLanguage.JAPANESE
) : BaseSourceField<MediaCharacterQuery>() {
    override fun toQueryOrMutation(): MediaCharacterQuery {
        return MediaCharacterQuery(
            page = nn(page),
            perPage = nn(perPage),
            mediaId = nn(mediaId),
            language = nn(language)
        )
    }
}