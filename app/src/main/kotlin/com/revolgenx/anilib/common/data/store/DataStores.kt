package com.revolgenx.anilib.common.data.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.revolgenx.anilib.browse.data.model.BrowseFilterModelSerializer
import com.revolgenx.anilib.list.data.filter.MediaListCollectionFilter
import com.revolgenx.anilib.list.data.filter.MediaListCollectionFilterSerializer
import com.revolgenx.anilib.media.data.constant.readableOn
import com.revolgenx.anilib.media.data.constant.streamingOn
import com.revolgenx.anilib.media.data.store.ExternalLinkSourceCollectionData
import com.revolgenx.anilib.media.data.store.ExternalLinkSourceCollectionDataSerializer
import com.revolgenx.anilib.media.data.store.MediaFilterData
import com.revolgenx.anilib.media.data.store.MediaFilterDataSerializer
import com.revolgenx.anilib.media.data.store.MediaTagCollectionData
import com.revolgenx.anilib.media.data.store.MediaTagCollectionDataSerializer
import com.revolgenx.anilib.media.ui.model.seasonFromMonth
import com.revolgenx.anilib.type.MediaSort
import java.time.LocalDateTime

typealias SeasonFilterDataStore = DataStore<MediaFilterData>
typealias MediaListFilterDataStore = DataStore<MediaListCollectionFilter>
typealias MediaTagCollectionDataStore = DataStore<MediaTagCollectionData>
typealias StreamingOnCollectionDataStore = DataStore<ExternalLinkSourceCollectionData>
typealias ReadableOnCollectionDataStore = DataStore<ExternalLinkSourceCollectionData>

val Context.browseFilterDataStore by dataStore(
    fileName = "browse_filter_data_store.json",
    serializer = BrowseFilterModelSerializer()
)

val Context.seasonFilterDataStore by dataStore(
    fileName = "season_filter_data_store.json",
    serializer = MediaFilterDataSerializer(
        MediaFilterData(
            seasonYear = LocalDateTime.now().year,
            season = seasonFromMonth(LocalDateTime.now().monthValue)
        )
    )
)

val Context.exploreTrendingDataStore by dataStore(
    fileName = "explore_trending_data_store.json",
    serializer = MediaFilterDataSerializer(MediaFilterData(sort = MediaSort.TRENDING_DESC))
)
val Context.explorePopularDataStore by dataStore(
    fileName = "explore_popular_data_store.json",
    serializer = MediaFilterDataSerializer(MediaFilterData(sort = MediaSort.POPULARITY_DESC))
)
val Context.exploreNewlyAddedDataStore by dataStore(
    fileName = "explore_newly_added_data_store.json",
    serializer = MediaFilterDataSerializer(MediaFilterData(sort = MediaSort.ID_DESC))
)

val Context.animeListFilterDataStore: MediaListFilterDataStore by dataStore(
    fileName = "anime_list_filter_data_store.json",
    serializer = MediaListCollectionFilterSerializer()
)
val Context.mangaListFilterDataStore: MediaListFilterDataStore by dataStore(
    fileName = "manga_list_filter_data_store.json",
    serializer = MediaListCollectionFilterSerializer()
)
val Context.mediaTagCollectionDataStore by dataStore(
    fileName = "media_tag_collection_data_store.json",
    serializer = MediaTagCollectionDataSerializer()
)


val Context.streamingOnCollectionDataStore by dataStore(
    fileName = "streaming_on_collection_data_store.json",
    serializer = ExternalLinkSourceCollectionDataSerializer(
        ExternalLinkSourceCollectionData(
            streamingOn
        )
    )
)


val Context.readableOnCollectionDataStore by dataStore(
    fileName = "readable_on_collection_data_store.json",
    serializer = ExternalLinkSourceCollectionDataSerializer(
        ExternalLinkSourceCollectionData(
            readableOn
        )
    )
)
