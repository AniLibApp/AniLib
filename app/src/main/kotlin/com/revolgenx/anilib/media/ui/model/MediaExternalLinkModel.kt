package com.revolgenx.anilib.media.ui.model

import kotlinx.serialization.Serializable

@Serializable
data class MediaExternalLinkModel(
    val id: Int = -1,
    val url: String? = null,
    val site: String? = null,
    val language: String? = null,
    val icon: String? = null,
    val color: Int? = null,
)