package com.revolgenx.anilib.browse.data.store

import androidx.datastore.preferences.core.stringPreferencesKey
import com.revolgenx.anilib.common.data.model.PreferenceDataStoreModel
import com.revolgenx.anilib.common.data.store.IPreferencesDataStore
import com.revolgenx.anilib.common.data.store.PreferencesDataStore

class BrowsePreferencesDataStore(override val dataStore: PreferencesDataStore) :
    IPreferencesDataStore {
    companion object {
        val browseSearchHistoryKey = stringPreferencesKey("browse_search_history_key")
    }

    val browseHistory = PreferenceDataStoreModel(
        dataStore = dataStore,
        prefKey = browseSearchHistoryKey,
        defaultValue = ""
    )
}