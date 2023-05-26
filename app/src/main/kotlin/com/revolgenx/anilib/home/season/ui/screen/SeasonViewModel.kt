package com.revolgenx.anilib.home.season.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ui.screen.PagingViewModel
import com.revolgenx.anilib.home.season.data.store.SeasonFieldData
import com.revolgenx.anilib.media.data.field.MediaField
import com.revolgenx.anilib.media.data.service.MediaService
import com.revolgenx.anilib.media.data.source.MediaPagingSource
import com.revolgenx.anilib.media.data.filter.MediaFilter
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.nextSeason
import com.revolgenx.anilib.media.ui.model.previousSeason
import com.revolgenx.anilib.type.MediaSeason


class SeasonViewModel(
    private val mediaService: MediaService,
    private val seasonFilterStore: DataStore<MediaFilter>
) :
    PagingViewModel<MediaModel, MediaField, MediaPagingSource>(initialize = false) {

    override var field by mutableStateOf(SeasonFieldData.default().toField())

    init {
        launch {
            seasonFilterStore.data.collect {
                field = it.toField()
                refresh()
            }
        }
    }

    override val pagingSource: MediaPagingSource
        get() = MediaPagingSource(this.field, mediaService)

    fun nextSeason() {
        launch {
            val season = field.season.nextSeason()
            var seasonYear = field.seasonYear
            if (season == MediaSeason.WINTER) {
                seasonYear = field.seasonYear!! + 1
            }
            seasonFilterStore.updateData {
                it.copy(
                    seasonYear = seasonYear,
                    season = season
                )
            }
        }
    }

    fun previousSeason() {
        launch {
            val season = field.season.previousSeason()
            var seasonYear = field.seasonYear
            if (season == MediaSeason.FALL) {
                seasonYear = field.seasonYear!! - 1
            }
            seasonFilterStore.updateData {
                it.copy(
                    seasonYear = seasonYear,
                    season = season
                )
            }
        }
    }

}