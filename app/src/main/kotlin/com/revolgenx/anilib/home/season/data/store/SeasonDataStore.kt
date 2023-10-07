package com.revolgenx.anilib.home.season.data.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.revolgenx.anilib.common.data.store.FieldDataStore
import com.revolgenx.anilib.media.data.filter.MediaFilter
import com.revolgenx.anilib.media.data.filter.MediaFilterSerializer
import com.revolgenx.anilib.media.ui.model.seasonFromMonth
import java.time.LocalDateTime


typealias SeasonFilterDataStore = DataStore<MediaFilter>

const val seasonFilterStoreFileName = "season_filter_data_store.json"
val Context.seasonFilterDataStore: SeasonFilterDataStore by dataStore(
    fileName = seasonFilterStoreFileName,
    serializer = MediaFilterSerializer(SeasonDataStore.defaultSeasonFilter)
)

class SeasonDataStore(dataStore: DataStore<MediaFilter>){
    companion object {
        val defaultSeasonFilter = MediaFilter(
            seasonYear = LocalDateTime.now().year,
            season = seasonFromMonth(LocalDateTime.now().monthValue)
        )
    }

    val filter = FieldDataStore(
        dataStore = dataStore
    )
}