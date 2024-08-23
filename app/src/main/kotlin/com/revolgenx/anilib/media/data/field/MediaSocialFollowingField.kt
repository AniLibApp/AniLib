package com.revolgenx.anilib.media.data.field

import com.revolgenx.anilib.MediaSocialFollowingQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField

data class MediaSocialFollowingField(var mediaId: Int) :
    BaseSourceField<MediaSocialFollowingQuery>() {
    override fun toQueryOrMutation(): MediaSocialFollowingQuery {
        return MediaSocialFollowingQuery(
            page = nn(page),
            perPage = nn(perPage),
            mediaId = nn(mediaId)
        )
    }
}