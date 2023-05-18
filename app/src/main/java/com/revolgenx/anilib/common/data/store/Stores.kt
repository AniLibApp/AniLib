package com.revolgenx.anilib.common.data.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.revolgenx.anilib.home.season.data.store.SeasonFilterSerializer
import com.revolgenx.anilib.list.data.filter.MediaListCollectionFilter
import com.revolgenx.anilib.list.data.filter.MediaListCollectionFilterSerializer
import com.revolgenx.anilib.media.data.filter.MediaFilter

typealias AppDataStore = DataStore<Preferences>
typealias SeasonFilterDataStore = DataStore<MediaFilter>
typealias MediaListFilterDataStore = DataStore<MediaListCollectionFilter>

val Context.appDataStore: AppDataStore by preferencesDataStore(name = "app_preferences.json")

const val seasonFilterStoreFileName = "season_filter_preferences.json"
val Context.seasonFilterDataStore: SeasonFilterDataStore by dataStore(
    fileName = seasonFilterStoreFileName,
    serializer = SeasonFilterSerializer()
)

const val animeListFilterStoreFileName = "anime_list_filter_preferences.json"
const val mangaListFilterStoreFileName = "manga_list_filter_preferences.json"

val Context.animeListFilterDataStore: MediaListFilterDataStore by dataStore(
    fileName = animeListFilterStoreFileName,
    serializer = MediaListCollectionFilterSerializer()
)

val Context.mangaListFilterDataStore: MediaListFilterDataStore by dataStore(
    fileName = mangaListFilterStoreFileName,
    serializer = MediaListCollectionFilterSerializer()
)




