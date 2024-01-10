package com.revolgenx.anilib.setting.data.store

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import anilib.i18n.R
import com.revolgenx.anilib.common.data.store.BasePreferencesDataStore
import com.revolgenx.anilib.common.data.model.PreferenceDataStoreModel
import com.revolgenx.anilib.common.data.store.PreferencesDataStore
import com.revolgenx.anilib.media.ui.model.MediaTitleModel
import com.revolgenx.anilib.setting.ui.component.ListPreferenceEntry


class MediaSettingsPreferencesDataStore(override val dataStore: PreferencesDataStore) :
    BasePreferencesDataStore {
    companion object {
        val mediaTitleTypeKey = intPreferencesKey("media_title_type_key")
        val displayAdultContentKey = booleanPreferencesKey("display_adult_content_key")
    }

    val mediaTitleType = PreferenceDataStoreModel(
        dataStore = dataStore,
        prefKey = mediaTitleTypeKey,
        defaultValue = MediaTitleModel.type_romaji
    )

    val displayAdultContent = PreferenceDataStoreModel(
        dataStore = dataStore,
        prefKey = displayAdultContentKey,
        defaultValue = false
    )
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