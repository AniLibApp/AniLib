package com.revolgenx.anilib.notification.data.store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import com.revolgenx.anilib.common.data.store.PreferencesDataStore

class NotificationDataStore(val dataStore: DataStore<Preferences>) {
    companion object {
        val lastShownNotificationIdKey = intPreferencesKey("last_shown_notification_id_key")
    }

    var lastShownNotificationId = PreferencesDataStore(
        dataStore = dataStore,
        prefKey = lastShownNotificationIdKey
    )
}