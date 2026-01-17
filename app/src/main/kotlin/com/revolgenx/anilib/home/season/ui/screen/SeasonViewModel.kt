package com.revolgenx.anilib.home.season.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.revolgenx.anilib.common.data.store.SeasonFilterDataStore
import com.revolgenx.anilib.common.ext.get
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.media.data.field.MediaField
import com.revolgenx.anilib.media.data.service.MediaService
import com.revolgenx.anilib.media.data.source.MediaPagingSource
import com.revolgenx.anilib.media.data.store.MediaFilterData
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.nextSeason
import com.revolgenx.anilib.media.ui.model.previousSeason
import com.revolgenx.anilib.type.MediaSeason


class SeasonViewModel(
    private val mediaService: MediaService,
    private val seasonFilterDataStore: SeasonFilterDataStore
) :
    PagingViewModel<MediaModel, MediaField?, MediaPagingSource>() {
    private var filter = seasonFilterDataStore.data.get()
    override var field by mutableStateOf(filter.toMediaField())
    var isFiltered by mutableStateOf(checkFilter)
    private val checkFilter: Boolean
        get() = filter.run {
            status != null
                || season != MediaFilterData.defaultSeason
                || seasonYear != MediaFilterData.defaultSeasonYear
                || sort != null
                || formatsIn.isNullOrEmpty().not()
                || genreIn.isNullOrEmpty().not()
                || tagIn.isNullOrEmpty().not()
                || tagNotIn.isNullOrEmpty().not()
                || isAdult != null
        }
    init {
        launch {
            seasonFilterDataStore.data.collect { newFilter ->
                if (filter == newFilter) return@collect

                filter = newFilter
                field = filter.toMediaField()
                isFiltered = checkFilter
                refresh()
            }
        }
    }

    override val pagingSource: MediaPagingSource
        get() = MediaPagingSource(this.field, mediaService)

    fun nextSeason() {
        launch {
            val season = filter.season.nextSeason()
            var seasonYear = filter.seasonYear
            if (season == MediaSeason.WINTER) {
                seasonYear = filter.seasonYear!! + 1
            }
            seasonFilterDataStore.updateData {
                it.copy(
                    seasonYear = seasonYear,
                    season = season
                )
            }
        }
    }

    fun previousSeason() {
        launch {
            val season = filter.season.previousSeason()
            var seasonYear = filter.seasonYear
            if (season == MediaSeason.FALL) {
                seasonYear = filter.seasonYear!! - 1
            }
            seasonFilterDataStore.updateData {
                it.copy(
                    seasonYear = seasonYear,
                    season = season
                )
            }
        }
    }

}