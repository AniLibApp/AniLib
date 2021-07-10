package com.revolgenx.anilib.data.field.media

import com.revolgenx.anilib.MediaSocialFollowingQuery
import com.revolgenx.anilib.data.field.BaseSourceField

class MediaSocialFollowingField : BaseSourceField<MediaSocialFollowingQuery>() {
    var mediaId: Int? = null
    override fun toQueryOrMutation(): MediaSocialFollowingQuery {
        return MediaSocialFollowingQuery.builder()
            .page(page)
            .perPage(perPage)
            .mediaId(mediaId)
            .build()
    }

}