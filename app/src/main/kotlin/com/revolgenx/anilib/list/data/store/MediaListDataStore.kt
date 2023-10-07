package com.revolgenx.anilib.list.data.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.revolgenx.anilib.list.data.filter.MediaListCollectionFilter
import com.revolgenx.anilib.list.data.filter.MediaListCollectionFilterSerializer

typealias MediaListFilterDataStore = DataStore<MediaListCollectionFilter>


const val animeListFilterStoreFileName = "anime_list_filter_data_store.json"
const val mangaListFilterStoreFileName = "manga_list_filter_data_store.json"

val Context.animeListFilterDataStore: MediaListFilterDataStore by dataStore(
    fileName = animeListFilterStoreFileName,
    serializer = MediaListCollectionFilterSerializer()
)

val Context.mangaListFilterDataStore: MediaListFilterDataStore by dataStore(
    fileName = mangaListFilterStoreFileName,
    serializer = MediaListCollectionFilterSerializer()
)

class MediaListDataStore(private val dataStore: MediaListFilterDataStore) {
    companion object{

    }

}