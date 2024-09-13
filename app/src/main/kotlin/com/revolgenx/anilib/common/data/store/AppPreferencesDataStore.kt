package com.revolgenx.anilib.common.data.store

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import anilib.i18n.R
import com.auth0.android.jwt.JWT
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.media.ui.model.MediaCoverImageModel
import com.revolgenx.anilib.media.ui.model.MediaTitleModel
import com.revolgenx.anilib.notification.data.store.NotificationDataStore
import com.revolgenx.anilib.common.data.constant.AdsInterval
import com.revolgenx.anilib.common.data.constant.ExploreSectionOrder
import com.revolgenx.anilib.common.data.constant.MainPageOrder
import com.revolgenx.anilib.common.ext.get
import com.revolgenx.anilib.setting.ui.component.ListPreferenceEntry
import com.revolgenx.anilib.type.AiringSort
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AppPreferencesDataStore(val dataStore: DataStore<Preferences>) {
    val data get() = dataStore.data

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
        val displayAdsIntervalKey = intPreferencesKey("display_ads_interval_key")
        val adsDisplayedDateTimeKey = longPreferencesKey("ads_displayed_date_time_key")
        val autoPlayGifKey = booleanPreferencesKey("auto_play_gif_key")
        val showUserAboutKey = booleanPreferencesKey("show_user_about_key")
        val exploreAiringOrderKey = intPreferencesKey("explore_airing_order_key")
        val exploreTrendingOrderKey = intPreferencesKey("explore_trending_order_key")
        val explorePopularOrderKey = intPreferencesKey("explore_popular_order_key")
        val exploreNewlyAddedOrderKey = intPreferencesKey("explore_newly_added_order_key")
        val exploreWatchingOrderKey = intPreferencesKey("explore_watching_order_key")
        val exploreReadingOrderKey = intPreferencesKey("explore_reading_order_key")
        val exploreAiringEnabledKey = booleanPreferencesKey("explore_airing_enabled_key")
        val exploreTrendingEnabledKey = booleanPreferencesKey("explore_trending_enabled_key")
        val explorePopularEnabledKey = booleanPreferencesKey("explore_popular_enabled_key")
        val exploreNewlyAddedEnabledKey = booleanPreferencesKey("explore_newly_added_enabled_key")
        val exploreWatchingEnabledKey = booleanPreferencesKey("explore_watching_enabled_key")
        val exploreReadingEnabledKey = booleanPreferencesKey("explore_reading_enabled_key")
        val exploreWatchingSortKey = intPreferencesKey("explore_watching_sort_key")
        val exploreReadingSortKey = intPreferencesKey("explore_reading_sort_key")

        val homePageOrderKey = intPreferencesKey("home_page_order_key")
        val animePageOrderKey = intPreferencesKey("anime_page_order_key")
        val mangaPageOrderKey = intPreferencesKey("manga_page_order_key")
        val activityPageOrderKey = intPreferencesKey("activity_page_order_key")


        val crashReportKey = booleanPreferencesKey("crash_report_key")
        val widgetIncludeAlreadyAiredKey = booleanPreferencesKey("widget_include_already_aired_key")
        val widgetOpenListEditorKey = booleanPreferencesKey("widget_open_list_editor_key")
        val widgetOnlyWatchingKey = booleanPreferencesKey("widget_only_watching_key")
        val widgetOnlyPlanningKey = booleanPreferencesKey("widget_only_planning_key")
        val widgetAiringSortKey = intPreferencesKey("widget_airing_sort_key")

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
        defaultValue = 1
    )

    val otherMediaListDisplayMode = PreferencesDataStore(
        dataStore = dataStore,
        prefKey = otherMediaListDisplayModeKey,
        defaultValue = 1
    )

    val displayAdsInterval = PreferencesDataStore(
        dataStore = dataStore,
        prefKey = displayAdsIntervalKey,
        defaultValue = AdsInterval.EVERY_DAY.value
    )

    val adsDisplayedDateTime = PreferencesDataStore(
        dataStore = dataStore,
        prefKey = adsDisplayedDateTimeKey,
        defaultValue = null
    )

    val autoPlayGif = PreferencesDataStore(
        dataStore = dataStore,
        prefKey = autoPlayGifKey,
        defaultValue = false
    )

    val showUserAbout = PreferencesDataStore(
        dataStore = dataStore,
        prefKey = showUserAboutKey,
        defaultValue = false
    )

    val crashReport = PreferencesDataStore(
        dataStore = dataStore,
        prefKey = crashReportKey,
        defaultValue = true
    )

    val widgetIncludeAlreadyAired = PreferencesDataStore(
        dataStore = dataStore,
        prefKey = widgetIncludeAlreadyAiredKey,
        defaultValue = false
    )

    val widgetOpenListEditor = PreferencesDataStore(
        dataStore = dataStore,
        prefKey = widgetOpenListEditorKey,
        defaultValue = false
    )

    val widgetOnlyWatching = PreferencesDataStore(
        dataStore = dataStore,
        prefKey = widgetOnlyWatchingKey,
        defaultValue = false
    )

    val widgetOnlyPlanning = PreferencesDataStore(
        dataStore = dataStore,
        prefKey = widgetOnlyPlanningKey,
        defaultValue = false
    )

    val widgetAiringSort = PreferencesDataStore(
        dataStore = dataStore,
        prefKey = widgetAiringSortKey,
        defaultValue = AiringSort.TIME.ordinal
    )

    val isLoggedIn = userId.data.map { it != null }

    @Composable
    fun isLoggedIn(): Boolean {
        return userId.collectAsState().value != null
    }

    fun getMainPageOrder(mainPageOrder: MainPageOrder): Int {
        return when(mainPageOrder){
            MainPageOrder.HOME -> data.map { it[homePageOrderKey] ?: 0 }.get()
            MainPageOrder.ANIME -> data.map { it[animePageOrderKey] ?: 1 }.get()
            MainPageOrder.MANGA -> data.map { it[mangaPageOrderKey] ?: 2 }.get()
            MainPageOrder.ACTIVITY -> data.map { it[activityPageOrderKey] ?: 3 }.get()
        }
    }

    suspend fun setMainPageOrder(mainPageOrder: MainPageOrder, order: Int){
        dataStore.edit {
            when(mainPageOrder){
                MainPageOrder.HOME -> it[homePageOrderKey] = order
                MainPageOrder.ANIME -> it[animePageOrderKey] = order
                MainPageOrder.MANGA -> it[mangaPageOrderKey] = order
                MainPageOrder.ACTIVITY -> it[activityPageOrderKey] = order
            }
        }
    }


    fun getExploreSectionOrder(exploreSectionOrder: ExploreSectionOrder): Int {
        return when(exploreSectionOrder){
            ExploreSectionOrder.AIRING -> data.map { it[exploreAiringOrderKey] ?: 0 }.get()
            ExploreSectionOrder.TRENDING -> data.map { it[exploreTrendingOrderKey] ?: 1 }.get()
            ExploreSectionOrder.POPULAR -> data.map { it[explorePopularOrderKey] ?: 2 }.get()
            ExploreSectionOrder.NEWLY_ADDED -> data.map { it[exploreNewlyAddedOrderKey] ?: 3 }.get()
            ExploreSectionOrder.WATCHING -> data.map { it[exploreWatchingOrderKey] ?: 4 }.get()
            ExploreSectionOrder.READING -> data.map { it[exploreReadingOrderKey] ?: 5 }.get()
        }
    }

    suspend fun setExploreSectionOrder(exploreSectionOrder: ExploreSectionOrder, order: Int){
        dataStore.edit {
            when(exploreSectionOrder){
                ExploreSectionOrder.AIRING -> it[exploreAiringOrderKey] = order
                ExploreSectionOrder.TRENDING -> it[exploreTrendingOrderKey] = order
                ExploreSectionOrder.POPULAR -> it[explorePopularOrderKey] = order
                ExploreSectionOrder.NEWLY_ADDED -> it[exploreNewlyAddedOrderKey] = order
                ExploreSectionOrder.WATCHING -> it[exploreWatchingOrderKey] = order
                ExploreSectionOrder.READING -> it[exploreReadingOrderKey] = order
            }
        }
    }

    fun isExploreSectionEnabled(exploreSectionOrder: ExploreSectionOrder): Boolean {
        return when(exploreSectionOrder){
            ExploreSectionOrder.AIRING -> data.map { it[exploreAiringEnabledKey] ?: true }.get()
            ExploreSectionOrder.TRENDING -> data.map { it[exploreTrendingEnabledKey] ?: true }.get()
            ExploreSectionOrder.POPULAR -> data.map { it[explorePopularEnabledKey] ?: true }.get()
            ExploreSectionOrder.NEWLY_ADDED -> data.map { it[exploreNewlyAddedEnabledKey] ?: true }.get()
            ExploreSectionOrder.WATCHING -> data.map { it[exploreWatchingEnabledKey] ?: true }.get()
            ExploreSectionOrder.READING -> data.map { it[exploreReadingEnabledKey] ?: true }.get()
        }
    }

    suspend fun setExploreSectionEnabled(exploreSectionOrder: ExploreSectionOrder,enabled: Boolean){
        dataStore.edit {
            when(exploreSectionOrder){
                ExploreSectionOrder.AIRING -> it[exploreAiringEnabledKey] = enabled
                ExploreSectionOrder.TRENDING -> it[exploreTrendingEnabledKey] = enabled
                ExploreSectionOrder.POPULAR -> it[explorePopularEnabledKey] = enabled
                ExploreSectionOrder.NEWLY_ADDED -> it[exploreNewlyAddedEnabledKey] = enabled
                ExploreSectionOrder.WATCHING -> it[exploreWatchingEnabledKey] = enabled
                ExploreSectionOrder.READING -> it[exploreReadingEnabledKey] = enabled
            }
        }
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
            pref.remove(widgetOpenListEditorKey)
            pref.remove(widgetOnlyWatchingKey)
            pref.remove(widgetOnlyPlanningKey)
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
    return when (this) {
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