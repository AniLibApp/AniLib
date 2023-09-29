package com.revolgenx.anilib.common.data.state

import com.revolgenx.anilib.media.ui.model.MediaCoverImageModel
import com.revolgenx.anilib.media.ui.model.MediaTitleModel

data class UserState(val userId: Int? = null) {
    val isLoggedIn = userId != null
}

data class MediaState(
    val titleType: Int = MediaTitleModel.type_romaji,
    val coverImageType: Int = MediaCoverImageModel.type_large
)