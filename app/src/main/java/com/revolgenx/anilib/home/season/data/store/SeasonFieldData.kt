package com.revolgenx.anilib.home.season.data.store

import com.revolgenx.anilib.media.data.filter.MediaFilter
import com.revolgenx.anilib.media.ui.model.seasonFromMonth
import java.time.LocalDateTime

class SeasonFieldData {
    companion object {
        fun default() = MediaFilter(
            seasonYear = LocalDateTime.now().year,
            season = seasonFromMonth(LocalDateTime.now().monthValue)
        )
    }
}