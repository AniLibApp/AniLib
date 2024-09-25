package com.revolgenx.anilib.app.ui.viewmodel

import android.text.Spanned
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import cafe.adriel.voyager.core.screen.Screen
import com.revolgenx.anilib.common.data.constant.MainPageOrder
import com.revolgenx.anilib.common.data.state.MediaState
import com.revolgenx.anilib.common.data.state.UserState
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore.Companion.mediaCoverImageTypeKey
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore.Companion.mediaTitleTypeKey
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore.Companion.userIdKey
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.media.ui.model.MediaCoverImageModel
import com.revolgenx.anilib.media.ui.model.MediaTitleModel.Companion.type_romaji
import com.revolgenx.anilib.setting.ui.viewmodel.ContentOrderData
import com.revolgenx.anilib.user.data.field.UserSettingsField
import com.revolgenx.anilib.user.data.service.UserService
import com.revolgenx.anilib.user.ui.model.toMediaTitleType
import kotlinx.coroutines.flow.single

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

class MainActivityViewModel(
    private val preferencesDataStore: AppPreferencesDataStore,
    private val userService: UserService
) :
    ViewModel() {
    var userState by mutableStateOf(UserState(userId = preferencesDataStore.userId.get()))
    var mediaState by mutableStateOf(
        MediaState(
            titleType = preferencesDataStore.mediaTitleType.get()!!,
            coverImageType = preferencesDataStore.mediaCoverImageType.get()!!
        )
    )

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
                val mediaCoverImageType =
                    it[mediaCoverImageTypeKey] ?: MediaCoverImageModel.type_large
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

    private fun getUserSettings(userId: Int) {
        launch {
            val userSettings = userService.getUserSettings(UserSettingsField().also {
                it.userId = userId
            }).single()

            userSettings?.let { settings ->
                settings.options?.let {
                    preferencesDataStore.mediaTitleType.set(it.titleLanguage.toMediaTitleType())
                }
            }
        }
    }
}
