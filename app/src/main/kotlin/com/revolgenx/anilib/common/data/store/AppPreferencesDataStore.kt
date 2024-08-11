package com.revolgenx.anilib.common.data.store

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import anilib.i18n.R
import com.auth0.android.jwt.JWT
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.media.ui.model.MediaCoverImageModel
import com.revolgenx.anilib.media.ui.model.MediaTitleModel
import com.revolgenx.anilib.notification.data.store.NotificationDataStore
import com.revolgenx.anilib.setting.ui.component.ListPreferenceEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AppPreferencesDataStore(val dataStore: DataStore<Preferences>) {

    companion object {
        val authTokenKey = stringPreferencesKey("auth_token_key")
        val userIdKey = intPreferencesKey("user_id_key")
        val notificationRefreshIntervalKey = intPreferencesKey("notification_refresh_interval_key")
        val mediaCoverImageTypeKey = intPreferencesKey("media_cover_image_type_key")
        val openMediaListEntryEditorOnClickKey =
            booleanPreferencesKey("open_media_list_entry_editor_on_click_key")
        val browseSearchHistoryKey = stringPreferencesKey("browse_search_history_key")
        val mediaListSearchHistoryKey = stringPreferencesKey("media_list_search_history_key")
        val mediaTitleTypeKey = intPreferencesKey("media_title_type_key")
        val displayAdultContentKey = booleanPreferencesKey("display_adult_content_key")
        val mediaListDisplayModeKey = intPreferencesKey("media_list_display_mode_key")
        val otherMediaListDisplayModeKey = intPreferencesKey("other_media_list_display_mode_key")
    }


    val token = PreferencesDataStore(
        dataStore = dataStore,
        prefKey = authTokenKey
    )

    val userId = PreferencesDataStore(
        dataStore = dataStore,
        prefKey = userIdKey
    )

    val mediaCoverImageType = PreferencesDataStore(
        dataStore = dataStore,
        prefKey = mediaCoverImageTypeKey,
        defaultValue = MediaCoverImageModel.type_large
    )

    val notificationRefreshInterval = PreferencesDataStore(
        dataStore = dataStore,
        prefKey = notificationRefreshIntervalKey,
        defaultValue = 15
    )

    val openMediaListEntryEditorOnClick = PreferencesDataStore(
        dataStore = dataStore,
        prefKey = openMediaListEntryEditorOnClickKey,
        defaultValue = false
    )


    val browseHistory = PreferencesDataStore(
        dataStore = dataStore,
        prefKey = browseSearchHistoryKey,
        defaultValue = ""
    )


    val mediaListSearchHistory = PreferencesDataStore(
        dataStore = dataStore,
        prefKey = mediaListSearchHistoryKey,
        defaultValue = ""
    )

    val mediaTitleType = PreferencesDataStore(
        dataStore = dataStore,
        prefKey = mediaTitleTypeKey,
        defaultValue = MediaTitleModel.type_romaji
    )

    val displayAdultContent = PreferencesDataStore(
        dataStore = dataStore,
        prefKey = displayAdultContentKey,
        defaultValue = false
    )

    val mediaListDisplayMode = PreferencesDataStore(
        dataStore = dataStore,
        prefKey = mediaListDisplayModeKey,
        defaultValue = 0
    )
   val otherMediaListDisplayMode = PreferencesDataStore(
        dataStore = dataStore,
        prefKey = otherMediaListDisplayModeKey,
        defaultValue = 0
    )


    val isLoggedIn = userId.data.map { it != null }

    @Composable
    fun isLoggedIn(): Boolean {
        return userId.collectAsState().value != null
    }

    fun continueIfLoggedIn(
        scope: CoroutineScope,
        callback: OnClick
    ) {
        scope.launch {
            userId.collect { id ->
                if (id != null) {
                    callback.invoke()
                }
            }
        }
    }

    suspend fun login(mToken: String) {
        val userId = JWT(mToken).subject!!.trim().toInt()
        dataStore.edit { pref ->
            pref[authTokenKey] = mToken
            pref[userIdKey] = userId
        }
    }

    suspend fun logout() {
        dataStore.edit { pref ->
            pref.remove(authTokenKey)
            pref.remove(userIdKey)
            pref.remove(NotificationDataStore.lastShownNotificationIdKey)
            pref.remove(displayAdultContentKey)
            pref[mediaTitleTypeKey] = MediaTitleModel.type_romaji
        }
    }
}

enum class MediaListDisplayMode(val value: Int) {
    LIST(0), LIST_COMPACT(1), GRID(2), GRID_COMPACT(3);

    companion object {
        fun fromValue(value: Int): MediaListDisplayMode {
            return entries.first { it.value == value }
        }
    }
}

fun MediaListDisplayMode.toStringRes(): Int {
    return when(this){
        MediaListDisplayMode.LIST -> R.string.list
        MediaListDisplayMode.LIST_COMPACT -> R.string.list_compact
        MediaListDisplayMode.GRID -> R.string.grid
        MediaListDisplayMode.GRID_COMPACT -> R.string.grid_compact
    }
}


fun mediaTitlePrefEntry(context: Context) = listOf(
    ListPreferenceEntry(
        title = context.getString(R.string.romaji),
        value = MediaTitleModel.type_romaji
    ),
    ListPreferenceEntry(
        title = context.getString(R.string.english),
        value = MediaTitleModel.type_english
    ),
    ListPreferenceEntry(
        title = context.getString(R.string._native),
        value = MediaTitleModel.type_native
    )
)

fun activityMergeTimePrefEntry(context: Context) = listOf(
    ListPreferenceEntry(
        title = context.getString(R.string.merge_time_never),
        value = 0
    ),
    ListPreferenceEntry(
        title = context.getString(R.string.merge_time_30_min),
        value = 30
    ),
    ListPreferenceEntry(
        title = context.getString(R.string.merge_time_1_hour),
        value = 60
    ),
    ListPreferenceEntry(
        title = context.getString(R.string.merge_time_2_hours),
        value = 120
    ),
    ListPreferenceEntry(
        title = context.getString(R.string.merge_time_3_hours),
        value = 180
    ),
    ListPreferenceEntry(
        title = context.getString(R.string.merge_time_6_hours),
        value = 360
    ),
    ListPreferenceEntry(
        title = context.getString(R.string.merge_time_12_hours),
        value = 720
    ),
    ListPreferenceEntry(
        title = context.getString(R.string.merge_time_1_day),
        value = 1440
    ),
    ListPreferenceEntry(
        title = context.getString(R.string.merge_time_2_days),
        value = 2880
    ),
    ListPreferenceEntry(
        title = context.getString(R.string.merge_time_3_days),
        value = 4320
    ),
    ListPreferenceEntry(
        title = context.getString(R.string.merge_time_1_week),
        value = 10080
    ),
    ListPreferenceEntry(
        title = context.getString(R.string.merge_time_2_weeks),
        value = 20160
    ),
    ListPreferenceEntry(
        title = context.getString(R.string.merge_time_always),
        value = 29160
    ),
)