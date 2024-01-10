package com.revolgenx.anilib.app.ui.viewmodel

import android.text.Spanned
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.common.data.state.MediaState
import com.revolgenx.anilib.common.data.state.UserState
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore.Companion.mediaCoverImageTypeKey
import com.revolgenx.anilib.common.data.store.AuthPreferencesDataStore.Companion.userIdKey
import com.revolgenx.anilib.common.data.store.PreferencesDataStore
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.media.ui.model.MediaCoverImageModel
import com.revolgenx.anilib.media.ui.model.MediaTitleModel.Companion.type_romaji
import com.revolgenx.anilib.setting.data.store.MediaSettingsPreferencesDataStore.Companion.mediaTitleTypeKey

class MainActivityViewModel(preferencesDataStore: PreferencesDataStore) : ViewModel() {
    var userState by mutableStateOf(UserState())
    var mediaState by mutableStateOf(MediaState())

    var openSpoilerBottomSheet = mutableStateOf(false)
    var spoilerSpanned by mutableStateOf<Spanned?>(null)

    init {
        launch {
            preferencesDataStore.data.collect {
                val userId = it[userIdKey]
                val mediaTitleType = it[mediaTitleTypeKey] ?: type_romaji
                val mediaCoverImageType = it[mediaCoverImageTypeKey] ?: MediaCoverImageModel.type_large
                userState = userState.copy(userId = userId)
                mediaState = mediaState.copy(
                    titleType = mediaTitleType,
                    coverImageType = mediaCoverImageType
                )
            }
        }
    }
}