package com.revolgenx.anilib.home.explore.ui.viewmodel

import androidx.datastore.core.DataStore
import com.revolgenx.anilib.media.data.store.MediaFilterData
import com.revolgenx.anilib.media.ui.viewmodel.MediaFilterBottomSheetViewModel


class ExploreTrendingFilterViewModel(
    dataStore: DataStore<MediaFilterData>
) : MediaFilterBottomSheetViewModel(dataStore)

class ExplorePopularFilterViewModel(
    dataStore: DataStore<MediaFilterData>
) : MediaFilterBottomSheetViewModel(dataStore)

class ExploreNewlyAddedFilterViewModel(
    dataStore: DataStore<MediaFilterData>
) : MediaFilterBottomSheetViewModel(dataStore)
