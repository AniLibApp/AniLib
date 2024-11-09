package com.revolgenx.anilib.home.explore.ui.viewmodel

import androidx.datastore.core.DataStore
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.data.store.GenreCollectionDataStore
import com.revolgenx.anilib.common.data.store.MediaTagCollectionDataStore
import com.revolgenx.anilib.media.data.store.MediaFilterData
import com.revolgenx.anilib.media.ui.viewmodel.MediaFilterBottomSheetViewModel


class ExploreTrendingFilterViewModel(
    dataStore: DataStore<MediaFilterData>,
    mediaTagCollectionDataStore: MediaTagCollectionDataStore,
    genreCollectionDataStore: GenreCollectionDataStore,
    appPreferencesDataStore: AppPreferencesDataStore
) : MediaFilterBottomSheetViewModel(dataStore, mediaTagCollectionDataStore, genreCollectionDataStore, appPreferencesDataStore)

class ExplorePopularFilterViewModel(
    dataStore: DataStore<MediaFilterData>,
    mediaTagCollectionDataStore: MediaTagCollectionDataStore,
    genreCollectionDataStore: GenreCollectionDataStore,
    appPreferencesDataStore: AppPreferencesDataStore
) : MediaFilterBottomSheetViewModel(dataStore, mediaTagCollectionDataStore, genreCollectionDataStore, appPreferencesDataStore)

class ExploreNewlyAddedFilterViewModel(
    dataStore: DataStore<MediaFilterData>,
    mediaTagCollectionDataStore: MediaTagCollectionDataStore,
    genreCollectionDataStore: GenreCollectionDataStore,
    appPreferencesDataStore: AppPreferencesDataStore
) : MediaFilterBottomSheetViewModel(dataStore, mediaTagCollectionDataStore, genreCollectionDataStore, appPreferencesDataStore)
