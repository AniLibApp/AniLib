package com.revolgenx.anilib.app.ui.viewmodel

import android.text.Spanned
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.common.data.constant.AdsInterval
import com.revolgenx.anilib.common.data.constant.MainPageOrder
import com.revolgenx.anilib.common.data.constant.showAds
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
import kotlinx.coroutines.delay
import java.time.Instant

enum class DeepLinkPath {
    ANIME,
    MANGA,
    USER,
    CHARACTER,
    STAFF,
    STUDIO,
    ACTIVITY,
    NOTIFICATION,
    AIRING
}

class MainActivityViewModel(private val preferencesDataStore: AppPreferencesDataStore) :
    ViewModel() {
    var userState by mutableStateOf(UserState(userId = preferencesDataStore.userId.get()))
    var mediaState by mutableStateOf(
        MediaState(
            titleType = preferencesDataStore.mediaTitleType.get()!!,
            coverImageType = preferencesDataStore.mediaCoverImageType.get()!!
        )
    )

    private val displayAdsInterval = preferencesDataStore.displayAdsInterval
    private val adsDisplayedDateTime = preferencesDataStore.adsDisplayedDateTime
    val mainPageOrder = MainPageOrder.entries.map { p ->
        ContentOrderData(
            value = p,
            order = preferencesDataStore.getMainPageOrder(p),
            isEnabled = true
        )
    }.sortedBy { it.order }

    val showAdsDialog = mutableStateOf(false)

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
            }
        }


        launch {
            delay(5000)
            val currentEpochSecond = Instant.now().epochSecond
            if (adsDisplayedDateTime.get() == null) {
                adsDisplayedDateTime.set(currentEpochSecond)
            } else {
                val showAds = showAds(
                    AdsInterval.fromValue(displayAdsInterval.get()!!),
                    currentEpochSecond,
                    adsDisplayedDateTime.get()!!
                )
                if (showAds) {
                    showAdsDialog.value = true
                    adsDisplayedDateTime.set(currentEpochSecond)
                }
            }
        }
    }
}