package com.revolgenx.anilib.media.ui.model

import com.revolgenx.anilib.common.ui.model.BaseModel
import kotlinx.serialization.Serializable

@Serializable
data class MediaGenreModel(
    val name: String,
    val isIncludedInAnime: Boolean = false,
    val isIncludedInManga: Boolean = false,
    val isExcludedInAnime: Boolean = false,
    val isExcludedInManga: Boolean = false
): BaseModel

