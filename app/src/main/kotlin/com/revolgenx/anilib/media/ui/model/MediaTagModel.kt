package com.revolgenx.anilib.media.ui.model

import kotlinx.serialization.Serializable


@Serializable
data class MediaTagModel(
    val id: Int? = null,
    val name: String,
    val description: String? = null,
    val category: String? = null,
    val isMediaSpoilerTag: Boolean = false,
    val rank: Int? = null,
    val isAdult: Boolean = false
)