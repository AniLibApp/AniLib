package com.revolgenx.anilib.common.data.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.revolgenx.anilib.media.data.store.MediaFieldData
import com.revolgenx.anilib.home.season.data.store.SeasonFieldSerializer

object StoreFileName {
    const val seasonFieldFileName = "season_field_preferences.json"
}

val Context.appPreferenceDataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences.json")

val Context.seasonFieldDataStore: DataStore<MediaFieldData> by dataStore(
    fileName = StoreFileName.seasonFieldFileName,
    serializer = SeasonFieldSerializer()
)