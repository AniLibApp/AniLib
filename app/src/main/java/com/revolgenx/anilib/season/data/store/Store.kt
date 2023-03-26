package com.revolgenx.anilib.season.data.store

import android.content.Context
import androidx.datastore.dataStore
import com.revolgenx.anilib.media.data.store.MediaFilterSerializer

object StoreFileName{
    const val seasonFilterFileName = "season_filter_preferences.json"
}

val Context.seasonFilterStore by dataStore(
    fileName = StoreFileName.seasonFilterFileName,
    serializer = MediaFilterSerializer()
)