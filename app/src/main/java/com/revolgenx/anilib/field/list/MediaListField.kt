package com.revolgenx.anilib.field.list

import com.revolgenx.anilib.MediaListCollectinoQuery
import com.revolgenx.anilib.field.BaseSourceField
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.MediaType

class MediaListField : BaseSourceField<MediaListCollectinoQuery>() {
    var userId: Int? = null
    var userName: String? = null
    var type: Int? = null
    var mediaListStatus: Int? = null

    override fun toQueryOrMutation(): MediaListCollectinoQuery {
        return MediaListCollectinoQuery.builder()
            .apply {
                userId?.let {
                    userId(it)
                }
                userName?.let {
                    userName(it)
                }
                type?.let {
                    type(MediaType.values()[it])
                }
                mediaListStatus?.let {
                    status(MediaListStatus.values()[it])
                }
                when (type) {
                    MediaType.ANIME.ordinal -> {
                        includeEpisode(true)
                        includeChapter(false)
                    }
                    MediaType.MANGA.ordinal -> {
                        includeChapter(true)
                        includeEpisode(false)
                    }
                }
            }.build()
    }
}
