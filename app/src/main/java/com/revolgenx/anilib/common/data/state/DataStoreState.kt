package com.revolgenx.anilib.common.data.state

import com.revolgenx.anilib.media.ui.model.MediaCoverImageType
import com.revolgenx.anilib.media.ui.model.MediaTitleType

data class UserState(val userId: Int? = null) {
    val isLoggedIn = userId != null
}

data class MediaState(
    val titleType: MediaTitleType = MediaTitleType.ROMAJI,
    val coverImageType: MediaCoverImageType = MediaCoverImageType.LARGE
)