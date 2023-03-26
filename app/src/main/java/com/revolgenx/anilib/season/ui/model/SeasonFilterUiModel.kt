package com.revolgenx.anilib.season.ui.model

import com.revolgenx.anilib.media.ui.model.seasonFromMonth
import com.revolgenx.anilib.type.MediaSeason
import java.time.LocalDateTime

data class SeasonFilterUiModel(
    val season: MediaSeason? = seasonFromMonth(LocalDateTime.now().monthValue),
    val seasonYear: Int? = LocalDateTime.now().year
){
}