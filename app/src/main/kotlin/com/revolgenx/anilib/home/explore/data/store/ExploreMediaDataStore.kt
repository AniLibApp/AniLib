package com.revolgenx.anilib.home.explore.data.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.revolgenx.anilib.common.data.store.FieldDataStore
import com.revolgenx.anilib.media.data.filter.MediaFilter
import com.revolgenx.anilib.media.data.filter.MediaFilterSerializer
import com.revolgenx.anilib.type.MediaSort


const val exploreTrendingDataStore = "explore_trending_data_store.json"
const val explorePopularDataStore = "explore_popular_data_store.json"
const val exploreNewlyAddedDataStore = "explore_newly_added_data_store.json"

val Context.exploreTrendingDataStore by dataStore(
    fileName = exploreTrendingDataStore,
    serializer = MediaFilterSerializer(MediaFilter(sort = MediaSort.TRENDING_DESC))
)

val Context.explorePopularDataStore by dataStore(
    fileName = explorePopularDataStore,
    serializer = MediaFilterSerializer(MediaFilter(sort = MediaSort.POPULARITY_DESC))
)

val Context.exploreNewlyAddedDataStore by dataStore(
    fileName = exploreNewlyAddedDataStore,
    serializer = MediaFilterSerializer(MediaFilter(sort = MediaSort.ID_DESC))
)

sealed class ExploreMediaDataStore(dataStore: DataStore<MediaFilter>) {
    val filter: FieldDataStore<MediaFilter> = FieldDataStore(dataStore)

    class ExploreTrendingDataStore(dataStore: DataStore<MediaFilter>) :
        ExploreMediaDataStore(dataStore)

    class ExplorePopularDataStore(dataStore: DataStore<MediaFilter>) :
        ExploreMediaDataStore(dataStore)

    class ExploreNewlyAddedDataStore(dataStore: DataStore<MediaFilter>) :
        ExploreMediaDataStore(dataStore)
}
