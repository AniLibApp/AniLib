package com.revolgenx.anilib.home.season.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.home.season.data.store.SeasonDataStore
import com.revolgenx.anilib.media.data.field.MediaField
import com.revolgenx.anilib.media.data.service.MediaService
import com.revolgenx.anilib.media.data.source.MediaPagingSource
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.nextSeason
import com.revolgenx.anilib.media.ui.model.previousSeason
import com.revolgenx.anilib.type.MediaSeason


class SeasonViewModel(
    private val mediaService: MediaService,
    private val seasonDataStore: SeasonDataStore
) :
    PagingViewModel<MediaModel, MediaField, MediaPagingSource>(initialize = false) {

    override var field by mutableStateOf(seasonDataStore.filter.get().toField())

    init {
        launch {
            seasonDataStore.filter.collect {
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
            seasonDataStore.filter.update {
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
            seasonDataStore.filter.update {
                it.copy(
                    seasonYear = seasonYear,
                    season = season
                )
            }
        }
    }

}