package com.revolgenx.anilib.media.ui.model

import com.revolgenx.anilib.common.ui.model.BaseModel

data class StreamingEpisodeModel(
    val title: String?,
    val thumbnail: String?,
    val url: String?,
    val site: String?
) : BaseModel
