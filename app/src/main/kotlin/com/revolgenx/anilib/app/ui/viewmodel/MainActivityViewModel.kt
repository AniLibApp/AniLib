package com.revolgenx.anilib.app.ui.viewmodel

import android.text.Spanned
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.common.data.state.MediaState
import com.revolgenx.anilib.common.data.state.UserState
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore.Companion.mediaCoverImageTypeKey
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore.Companion.mediaTitleTypeKey
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore.Companion.userIdKey
import com.revolgenx.anilib.common.ext.get
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.media.ui.model.MediaCoverImageModel
import com.revolgenx.anilib.media.ui.model.MediaTitleModel.Companion.type_romaji
import kotlinx.coroutines.flow.map

enum class DeepLinkPath {
    ANIME,
    MANGA,
    USER,
    CHARACTER,
    STAFF,
    STUDIO,
    ACTIVITY,
    NOTIFICATION
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
    }
}