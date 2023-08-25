package com.revolgenx.anilib.media.ui.model

import com.revolgenx.anilib.common.data.constant.DAILYMOTION_URL
import com.revolgenx.anilib.common.data.constant.YOUTUBE_URL

data class MediaTrailerModel(
    val id: String?,
    val site: String?,
    val thumbnail: String?,
) {
    val source: TrailerSource = TrailerSource.from(site)
    val url: String = when (source) {
        TrailerSource.YOUTUBE -> YOUTUBE_URL + id
        TrailerSource.DAILYMOTION -> DAILYMOTION_URL + id
        TrailerSource.UNKNOWN -> site ?: ""
    }
}

enum class TrailerSource {
    YOUTUBE, DAILYMOTION, UNKNOWN;

    companion object {
        fun from(site: String?): TrailerSource {
            return when (site) {
                com.revolgenx.anilib.common.data.constant.YOUTUBE -> YOUTUBE
                com.revolgenx.anilib.common.data.constant.DAILYMOTION -> DAILYMOTION
                else -> UNKNOWN
            }
        }
    }
}