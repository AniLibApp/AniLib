package com.revolgenx.anilib.home.season.data.store

import com.revolgenx.anilib.media.data.store.MediaFieldData
import com.revolgenx.anilib.media.ui.model.seasonFromMonth
import java.time.LocalDateTime

class SeasonFieldData {
    companion object {
        fun default() = MediaFieldData(
            seasonYear = LocalDateTime.now().year,
            season = seasonFromMonth(LocalDateTime.now().monthValue)
        )
    }
}