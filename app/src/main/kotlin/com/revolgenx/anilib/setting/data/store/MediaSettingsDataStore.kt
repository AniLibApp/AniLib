package com.revolgenx.anilib.setting.data.store

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import com.revolgenx.anilib.common.data.store.BasePreferenceDataStore
import com.revolgenx.anilib.common.data.store.PreferencesDataStore
import com.revolgenx.anilib.common.data.store.PreferenceDataModel


class MediaSettingsDataStore(override val dataStore: PreferencesDataStore) :
    BasePreferenceDataStore() {
    companion object {
        val mediaTitleTypeKey = intPreferencesKey("media_title_type_key")
        val showAdultContentKey = booleanPreferencesKey("show_adult_content_key")
        val airingAnimeNotificationKey = booleanPreferencesKey("airing_anime_notification_key")
        const val romajiTitle = 0
    }

    val mediaTitleType = PreferenceDataModel(
        dataStore = dataStore,
        prefKey = mediaTitleTypeKey,
        defaultValue = romajiTitle
    )

    val showAdultContent = PreferenceDataModel(
        dataStore = dataStore,
        prefKey = showAdultContentKey,
        defaultValue = false
    )
    val airingAnimeNotification = PreferenceDataModel(
        dataStore = dataStore,
        prefKey = airingAnimeNotificationKey,
        defaultValue = false
    )
}


val activityMergeTime = listOf(0)