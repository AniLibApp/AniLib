package com.revolgenx.anilib.home.season.ui.viewmodel

import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.data.store.GenreCollectionDataStore
import com.revolgenx.anilib.common.data.store.MediaTagCollectionDataStore
import com.revolgenx.anilib.common.data.store.SeasonFilterDataStore
import com.revolgenx.anilib.media.ui.viewmodel.MediaFilterBottomSheetViewModel

class SeasonFilterViewModel(
    seasonFilterDataStore: SeasonFilterDataStore,
    mediaTagCollectionDataStore: MediaTagCollectionDataStore,
    genreCollectionDataStore: GenreCollectionDataStore,
    appPreferencesDataStore: AppPreferencesDataStore
) :
    MediaFilterBottomSheetViewModel(seasonFilterDataStore, mediaTagCollectionDataStore, genreCollectionDataStore, appPreferencesDataStore)