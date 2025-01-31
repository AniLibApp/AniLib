package com.revolgenx.anilib.app.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.text.Spanned
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import com.revolgenx.anilib.app.ui.activity.MainActivity
import com.revolgenx.anilib.common.data.constant.MainPageOrder
import com.revolgenx.anilib.common.data.state.MediaState
import com.revolgenx.anilib.common.data.state.UserState
import com.revolgenx.anilib.common.data.store.AiringScheduleFilterDataStore
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore.Companion.mediaCoverImageTypeKey
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore.Companion.mediaTitleTypeKey
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore.Companion.userIdKey
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.home.ui.screen.HomeScreen
import com.revolgenx.anilib.list.ui.screen.AnimeListScreen
import com.revolgenx.anilib.list.ui.screen.MangaListScreen
import com.revolgenx.anilib.media.ui.model.MediaCoverImageModel
import com.revolgenx.anilib.media.ui.model.MediaTitleModel.Companion.type_romaji
import com.revolgenx.anilib.setting.ui.screen.SettingScreen
import com.revolgenx.anilib.setting.ui.viewmodel.ContentOrderData
import com.revolgenx.anilib.social.ui.screen.ActivityUnionScreen
import com.revolgenx.anilib.user.data.field.UserSettingsField
import com.revolgenx.anilib.user.data.service.UserService
import com.revolgenx.anilib.user.ui.model.toMediaTitleType
import com.revolgenx.anilib.user.ui.screen.UserScreen
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

enum class DeepLinkPath {
    HOME,
    ANIME,
    MANGA,
    USER,
    CHARACTER,
    STAFF,
    STUDIO,
    ACTIVITY,
    NOTIFICATION,
    AIRING,
    ANIME_LIST,
    MANGA_LIST,
    LIST_ENTRY_EDITOR
}

val userScreen: UserScreen = UserScreen(isTab = true)

val mainScreenTabs = listOf(
    HomeScreen,
    AnimeListScreen,
    MangaListScreen,
    ActivityUnionScreen,
    SettingScreen,
    userScreen
)


class MainActivityViewModel(
    private val preferencesDataStore: AppPreferencesDataStore,
    private val userService: UserService,
    private val airingScheduleFilterDataStore: AiringScheduleFilterDataStore,
    private val exploreAiringScheduleFilterDataStore: AiringScheduleFilterDataStore
) :
    ViewModel() {
    val currentAppVersion = preferencesDataStore.currentAppVersion
    var userState by mutableStateOf(UserState(userId = preferencesDataStore.userId.get()))
    var mediaState by mutableStateOf(
        MediaState(
            titleType = preferencesDataStore.mediaTitleType.get()!!,
            coverImageType = preferencesDataStore.mediaCoverImageType.get()!!
        )
    )

    var tabWrapperNavigator: Navigator? = null

    var previousScreen: Screen? = null

    val mainPageOrder = MainPageOrder.entries.map { p ->
        ContentOrderData(
            value = p,
            order = preferencesDataStore.getMainPageOrder(p),
            isEnabled = true
        )
    }.sortedBy { it.order }

    var openSpoilerBottomSheet = mutableStateOf(false)
    var spoilerSpanned by mutableStateOf<Spanned?>(null)

    var isNotificationPermissionChecked = false

    val deepLinkPath = mutableStateOf<Pair<DeepLinkPath, Any>?>(null)

    init {
        launch {
            preferencesDataStore.dataStore.data.collect {
                val userId = it[userIdKey]
                val mediaTitleType = it[mediaTitleTypeKey] ?: type_romaji
                val mediaCoverImageType = it[mediaCoverImageTypeKey] ?: MediaCoverImageModel.type_extra_large
                userState = userState.copy(userId = userId)
                mediaState = mediaState.copy(
                    titleType = mediaTitleType,
                    coverImageType = mediaCoverImageType
                )

                userId?.let {
                    getUserSettings(it)
                }
            }
        }
    }

    @OptIn(InternalVoyagerApi::class)
    fun disposeTabs() {
        mainScreenTabs.forEach { tabs ->
            tabWrapperNavigator?.dispose(tabs)
        }
    }

    private fun getUserSettings(userId: Int) {
        userService.getUserSettings(UserSettingsField().also {
            it.userId = userId
        }).onEach { userModel ->
            userModel?.let { settings ->
                settings.options?.let {
                    preferencesDataStore.mediaTitleType.set(it.titleLanguage.toMediaTitleType())
                }
            }
        }.catch {}
            .launchIn(viewModelScope)
    }


    fun logout(context: Context){
        launch {
            preferencesDataStore.logout()
            airingScheduleFilterDataStore.updateData {
                it.copy(showOnlyPlanning = false, showOnlyWatching = false)
            }

            exploreAiringScheduleFilterDataStore.updateData {
                it.copy(showOnlyPlanning = false, showOnlyWatching = false)
            }

            context.startActivity(Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })

            if (context is MainActivity) {
                context.finish()
            }
        }
    }
}
