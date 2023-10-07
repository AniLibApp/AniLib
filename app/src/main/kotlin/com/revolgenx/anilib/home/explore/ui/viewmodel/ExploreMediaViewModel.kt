package com.revolgenx.anilib.home.explore.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.home.explore.data.store.ExploreMediaDataStore
import com.revolgenx.anilib.media.data.field.MediaField
import com.revolgenx.anilib.media.data.service.MediaService
import com.revolgenx.anilib.media.data.source.MediaPagingSource
import com.revolgenx.anilib.media.ui.model.MediaModel

sealed class ExploreMediaViewModel(
    private val mediaService: MediaService,
    private val dataStore: ExploreMediaDataStore
) :
    PagingViewModel<MediaModel, MediaField, MediaPagingSource>() {
    override var field: MediaField by mutableStateOf(dataStore.filter.get().toField())

    init {
        launch {
            dataStore.filter.collect {
                field = it.toField()
                refresh()
            }
        }
    }

    override val pagingSource: MediaPagingSource
        get() = MediaPagingSource(this.field, mediaService)

    class ExploreTrendingViewModel(
        mediaService: MediaService,
        dataStore: ExploreMediaDataStore.ExploreTrendingDataStore
    ) : ExploreMediaViewModel(mediaService, dataStore)

    class ExplorePopularViewModel(
        mediaService: MediaService,
        dataStore: ExploreMediaDataStore.ExplorePopularDataStore
    ) : ExploreMediaViewModel(mediaService, dataStore)

    class ExploreNewlyAddedViewModel(
        mediaService: MediaService,
        dataStore: ExploreMediaDataStore.ExploreNewlyAddedDataStore
    ) : ExploreMediaViewModel(mediaService, dataStore)
}