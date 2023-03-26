package com.revolgenx.anilib.season.ui.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.revolgenx.anilib.MediaQuery
import com.revolgenx.anilib.common.ui.screen.PagingViewModel
import com.revolgenx.anilib.common.ui.state.InitializationState
import com.revolgenx.anilib.media.data.source.MediaPagingSource
import com.revolgenx.anilib.media.data.model.MediaFilterModel
import com.revolgenx.anilib.media.data.service.MediaService
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.nextSeason
import com.revolgenx.anilib.media.ui.model.previousSeason
import com.revolgenx.anilib.media.ui.model.toModel
import com.revolgenx.anilib.season.ui.model.SeasonFilterUiModel
import com.revolgenx.anilib.type.MediaSeason
import kotlinx.coroutines.launch


class SeasonViewModel(private val service: MediaService, private val seasonFilterStore: DataStore<MediaFilterModel>) :
    PagingViewModel<MediaModel, MediaQuery.Medium>() {
    var filter = MediaFilterModel.default()
    val seasonFilterUiModel = mutableStateOf(SeasonFilterUiModel())
    val initializationState: MutableState<InitializationState> = mutableStateOf(InitializationState.Initializing)

    init {
        viewModelScope.launch {
            seasonFilterStore.data.collect{
                filter = it
                seasonFilterUiModel.value = SeasonFilterUiModel(
                    filter.season,
                    filter.seasonYear
                )
                if(initializationState.value == InitializationState.Initializing){
                    initializationState.value = InitializationState.Completed
                }else{
                    refresh()
                }
            }
        }
    }

    override val pagingSource: MediaPagingSource
        get() = MediaPagingSource(filter, service)

    override fun mapToUiModel(pagingData: PagingData<MediaQuery.Medium>) =
        pagingData.map {
            it.media.toModel()
        }


    fun nextSeason(){
        viewModelScope.launch {
            val season = filter.season.nextSeason()
            var seasonYear = filter.seasonYear
            if(season == MediaSeason.WINTER){
                seasonYear = filter.seasonYear!! + 1
            }
            seasonFilterStore.updateData {
                it.copy(
                    seasonYear = seasonYear,
                    season = season
                )
            }
        }
    }

    fun previousSeason(){
        viewModelScope.launch {
            val season = filter.season.previousSeason()
            var seasonYear = filter.seasonYear
            if(season == MediaSeason.FALL){
                seasonYear = filter.seasonYear!! - 1
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