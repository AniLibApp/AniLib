package com.revolgenx.anilib.home.explore.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import com.revolgenx.anilib.common.ext.get
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.media.data.field.MediaField
import com.revolgenx.anilib.media.data.service.MediaService
import com.revolgenx.anilib.media.data.source.MediaPagingSource
import com.revolgenx.anilib.media.data.store.MediaFilterData
import com.revolgenx.anilib.media.ui.model.MediaModel

sealed class ExploreMediaViewModel(
    private val mediaService: MediaService,
    private val dataStore: DataStore<MediaFilterData>
) :
    PagingViewModel<MediaModel, MediaField, MediaPagingSource>() {
    private var filter = dataStore.data.get()
    override var field: MediaField = filter.toMediaField()

    var isFiltered by mutableStateOf(checkFiltered)
    val checkFiltered
        get() = field.run {
            seasonYear != null
                    || year != null
                    || season != null
                    || status != null
                    || formatsIn.isNullOrEmpty().not()
                    || genreIn.isNullOrEmpty().not()
                    || tagIn.isNullOrEmpty().not()
                    || genreNotIn.isNullOrEmpty().not()
                    || tagNotIn.isNullOrEmpty().not()
                    || isAdult != null
        }

    init {
        launch {
            dataStore.data.collect { newFilter ->
                if (filter == newFilter) return@collect

                filter = newFilter
                field = newFilter.toMediaField()
                isFiltered = checkFiltered
                refresh()
            }
        }
    }

    override val pagingSource: MediaPagingSource
        get() = MediaPagingSource(this.field, mediaService)

    class ExploreTrendingViewModel(
        mediaService: MediaService,
        dataStore: DataStore<MediaFilterData>
    ) : ExploreMediaViewModel(mediaService, dataStore)

    class ExplorePopularViewModel(
        mediaService: MediaService,
        dataStore: DataStore<MediaFilterData>
    ) : ExploreMediaViewModel(mediaService, dataStore)

    class ExploreNewlyAddedViewModel(
        mediaService: MediaService,
        dataStore: DataStore<MediaFilterData>
    ) : ExploreMediaViewModel(mediaService, dataStore)
}