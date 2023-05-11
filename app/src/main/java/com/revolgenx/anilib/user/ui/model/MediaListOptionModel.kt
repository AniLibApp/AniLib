package com.revolgenx.anilib.user.ui.model

import com.revolgenx.anilib.type.ScoreFormat


data class MediaListOptionModel(
    val scoreFormat: ScoreFormat? = null,
    val rowOrder: Int? = null,
    val animeList: MediaListOptionTypeModel? = null,
    val mangaList: MediaListOptionTypeModel? = null,
)
