package com.revolgenx.anilib.media.data.field

import com.revolgenx.anilib.MediaSocialFollowingQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField

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