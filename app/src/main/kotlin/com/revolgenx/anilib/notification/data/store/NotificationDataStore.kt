package com.revolgenx.anilib.notification.data.store

import androidx.datastore.preferences.core.intPreferencesKey
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.data.store.PreferencesDataStore

class NotificationDataStore(val dataStore: PreferencesDataStore) {
    companion object {
        val lastShownNotificationIdKey = intPreferencesKey("last_shown_notification_id_key")
    }

    var lastShownNotificationId = AppPreferencesDataStore(
        dataStore = dataStore,
        prefKey = lastShownNotificationIdKey
    )
}