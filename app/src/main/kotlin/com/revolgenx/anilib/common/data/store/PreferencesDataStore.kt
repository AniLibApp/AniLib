package com.revolgenx.anilib.common.data.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

typealias PreferencesDataStore = DataStore<Preferences>

val Context.preferencesDataStore: PreferencesDataStore by preferencesDataStore(name = "preferences_data_store.json")

interface IPreferencesDataStore {
    val dataStore: PreferencesDataStore
}