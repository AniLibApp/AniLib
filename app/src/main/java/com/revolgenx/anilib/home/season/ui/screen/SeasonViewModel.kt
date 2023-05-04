package com.revolgenx.anilib.home.season.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.lifecycle.viewModelScope
import com.revolgenx.anilib.common.ui.screen.PagingViewModel
import com.revolgenx.anilib.media.data.source.MediaPagingSource
import com.revolgenx.anilib.media.data.field.MediaField
import com.revolgenx.anilib.media.data.service.MediaService
import com.revolgenx.anilib.media.data.store.MediaFieldData
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.nextSeason
import com.revolgenx.anilib.media.ui.model.previousSeason
import com.revolgenx.anilib.media.ui.model.seasonFromMonth
import com.revolgenx.anilib.type.MediaSeason
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime


class SeasonViewModel(
    private val mediaService: MediaService,
    private val seasonFilterStore: DataStore<MediaFieldData>
) :
    PagingViewModel<MediaModel, MediaField, MediaPagingSource>(initialize = false) {

    override var field by mutableStateOf(
        MediaField(
            seasonYear = LocalDateTime.now().year,
            season = seasonFromMonth(LocalDateTime.now().monthValue)
        )
    )

    init {
        viewModelScope.launch {
            field = seasonFilterStore.data.first().toFieldIfDifferent(field)
            refresh()
            seasonFilterStore.data.collect {
                if(!it.compare(field)){
                    field = it.toField()
                    refresh()
                }
            }
        }
    }

    override val pagingSource: MediaPagingSource
        get() = MediaPagingSource(this.field, mediaService)

    fun nextSeason() {
        viewModelScope.launch {
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
        viewModelScope.launch {
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