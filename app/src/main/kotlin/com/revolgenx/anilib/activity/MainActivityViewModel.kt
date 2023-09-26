package com.revolgenx.anilib.activity

import android.text.Spanned
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.common.data.state.MediaState
import com.revolgenx.anilib.common.data.state.UserState
import com.revolgenx.anilib.common.data.store.AppDataStore.Companion.mediaCoverImageTypeKey
import com.revolgenx.anilib.common.data.store.AppDataStore.Companion.mediaTitleTypeKey
import com.revolgenx.anilib.common.data.store.AuthDataStore.Companion.userIdKey
import com.revolgenx.anilib.common.data.store.PreferencesDataStore
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.media.ui.model.MediaCoverImageType
import com.revolgenx.anilib.media.ui.model.MediaTitleType

class MainActivityViewModel(preferencesDataStore: PreferencesDataStore) : ViewModel() {
    var userState by mutableStateOf(UserState())
    var mediaState by mutableStateOf(MediaState())

    var openSpoilerBottomSheet = mutableStateOf(false)
    var spoilerSpanned by mutableStateOf<Spanned?>(null)

    init {
        launch {
            preferencesDataStore.data.collect {
                val userId = it[userIdKey]
                val mediaTitleType = MediaTitleType.values()[it[mediaTitleTypeKey] ?: 0]
                val mediaCoverImageType =
                    MediaCoverImageType.values()[it[mediaCoverImageTypeKey] ?: 1]
                userState = userState.copy(userId = userId)
                mediaState = mediaState.copy(
                    titleType = mediaTitleType,
                    coverImageType = mediaCoverImageType
                )
            }
        }
    }
}