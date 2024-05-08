package com.revolgenx.anilib.browse.data.store

import androidx.datastore.preferences.core.stringPreferencesKey
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.data.store.PreferencesDataStore

class BrowsePreferencesDataStore(val dataStore: PreferencesDataStore) {
    companion object {
        val browseSearchHistoryKey = stringPreferencesKey("browse_search_history_key")
    }

    val browseHistory = AppPreferencesDataStore(
        dataStore = dataStore,
        prefKey = browseSearchHistoryKey,
        defaultValue = ""
    )
}