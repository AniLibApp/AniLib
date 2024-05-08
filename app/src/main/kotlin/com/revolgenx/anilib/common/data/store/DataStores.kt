package com.revolgenx.anilib.common.data.store

import android.content.Context
import android.os.Build
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.revolgenx.anilib.airing.data.store.AiringScheduleFilterData
import com.revolgenx.anilib.airing.data.store.AiringScheduleFilterDataSerializer
import com.revolgenx.anilib.browse.data.store.BrowseFilterData
import com.revolgenx.anilib.browse.data.store.BrowseFilterDataSerializer
import com.revolgenx.anilib.common.data.store.theme.ThemeData
import com.revolgenx.anilib.common.data.store.theme.ThemeDataSerializer
import com.revolgenx.anilib.common.data.store.theme.isDarkMode
import com.revolgenx.anilib.common.ui.theme.defaultDarkTheme
import com.revolgenx.anilib.common.ui.theme.defaultTheme
import com.revolgenx.anilib.list.data.filter.MediaListCollectionFilter
import com.revolgenx.anilib.list.data.filter.MediaListCollectionFilterSerializer
import com.revolgenx.anilib.media.data.constant.readableOn
import com.revolgenx.anilib.media.data.constant.streamingOn
import com.revolgenx.anilib.media.data.store.ExternalLinkSourceCollectionData
import com.revolgenx.anilib.media.data.store.ExternalLinkSourceCollectionDataSerializer
import com.revolgenx.anilib.media.data.store.GenreCollectionData
import com.revolgenx.anilib.media.data.store.GenreCollectionDataSerializer
import com.revolgenx.anilib.media.data.store.MediaFilterData
import com.revolgenx.anilib.media.data.store.MediaFilterDataSerializer
import com.revolgenx.anilib.media.data.store.MediaTagCollectionData
import com.revolgenx.anilib.media.data.store.MediaTagCollectionDataSerializer
import com.revolgenx.anilib.media.ui.model.seasonFromMonth
import com.revolgenx.anilib.social.data.data.ActivityUnionFilterData
import com.revolgenx.anilib.social.data.data.ActivityUnionFilterDataSerializer
import com.revolgenx.anilib.type.MediaSort
import java.time.LocalDateTime

typealias SeasonFilterDataStore = DataStore<MediaFilterData>
typealias MediaListFilterDataStore = DataStore<MediaListCollectionFilter>
typealias MediaTagCollectionDataStore = DataStore<MediaTagCollectionData>
typealias GenreCollectionDataStore = DataStore<GenreCollectionData>
typealias StreamingOnCollectionDataStore = DataStore<ExternalLinkSourceCollectionData>
typealias ReadableOnCollectionDataStore = DataStore<ExternalLinkSourceCollectionData>
typealias BrowseFilterDataStore = DataStore<BrowseFilterData>
typealias AiringScheduleFilterDataStore = DataStore<AiringScheduleFilterData>
typealias ActivityUnionFilterDataStore = DataStore<ActivityUnionFilterData>

val Context.browseFilterDataStore by dataStore(
    fileName = "browse_filter_data_store.json",
    serializer = BrowseFilterDataSerializer()
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
val Context.genreCollectionDataStore by dataStore(
    fileName = "genre_collection_data_store.json",
    serializer = GenreCollectionDataSerializer()
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

val Context.airingScheduleFilterDataStore by dataStore(
    fileName = "airing_schedule_filter_data_store.json",
    serializer = AiringScheduleFilterDataSerializer()
)

val Context.exploreAiringScheduleFilterDataStore by dataStore(
    fileName = "explore_airing_schedule_filter_data_store.json",
    serializer = AiringScheduleFilterDataSerializer()
)

val Context.activityUnionFilterDataStore by dataStore(
    fileName = "activity_union_filter_data_store.json",
    serializer = ActivityUnionFilterDataSerializer()
)
typealias PreferencesDataStore = DataStore<Preferences>

val Context.preferencesDataStore: PreferencesDataStore by preferencesDataStore(name = "preferences_data_store.json")
val Context.themeDataStore: DataStore<ThemeData>
    get() {
        val isDark = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) this.isDarkMode else false
        return DataStoreFactory.create(
            serializer = ThemeDataSerializer(if(isDark) defaultDarkTheme else defaultTheme),
            produceFile = { applicationContext.dataStoreFile("theme_data_store.json") }
        )
    }