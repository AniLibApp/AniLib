package com.revolgenx.anilib.home.explore.ui.viewmodel

import androidx.datastore.core.DataStore
import com.revolgenx.anilib.common.ext.collectIfNew
import com.revolgenx.anilib.common.ext.get
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.media.data.field.MediaField
import com.revolgenx.anilib.media.data.store.MediaFilterData
import com.revolgenx.anilib.media.data.service.MediaService
import com.revolgenx.anilib.media.data.source.MediaPagingSource
import com.revolgenx.anilib.media.ui.model.MediaModel

sealed class ExploreMediaViewModel(
    private val mediaService: MediaService,
    private val dataStore: DataStore<MediaFilterData>
) :
    PagingViewModel<MediaModel, MediaField, MediaPagingSource>() {
    private var filter = dataStore.data.get()
    override var field: MediaField = filter.toMediaField()

    init {
        launch {
            dataStore.data.collectIfNew(filter) { newFilter ->
                filter = newFilter
                field = newFilter.toMediaField()
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