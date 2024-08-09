package com.revolgenx.anilib.common.data.store

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import com.revolgenx.anilib.media.ui.model.MediaCoverImageModel


class GeneralPreferencesDataStore(val dataStore: PreferencesDataStore){
    companion object{
        val mediaCoverImageTypeKey = intPreferencesKey("media_cover_image_type_key")
        val notificationRefreshIntervalKey = intPreferencesKey("notification_refresh_interval_key")
        val openMediaListEntryEditorOnClickKey = booleanPreferencesKey("open_media_list_entry_editor_on_click_key")
    }

    val mediaCoverImageType = AppPreferencesDataStore(
        dataStore = dataStore,
        prefKey = mediaCoverImageTypeKey,
        defaultValue = MediaCoverImageModel.type_large
    )

    val notificationRefreshInterval = AppPreferencesDataStore(
        dataStore = dataStore,
        prefKey = notificationRefreshIntervalKey,
        defaultValue = 15
    )

    val openMediaListEntryEditorOnClick = AppPreferencesDataStore(
        dataStore = dataStore,
        prefKey = openMediaListEntryEditorOnClickKey,
        defaultValue = false
    )

}

