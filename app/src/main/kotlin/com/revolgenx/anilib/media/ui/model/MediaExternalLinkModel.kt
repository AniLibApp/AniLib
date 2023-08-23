package com.revolgenx.anilib.media.ui.model

import androidx.compose.ui.graphics.Color

data class MediaExternalLinkModel(
    val id: Int = -1,
    val url: String? = null,
    val site: String? = null,
    val icon: String? = null,
    val color: Color? = null,
)