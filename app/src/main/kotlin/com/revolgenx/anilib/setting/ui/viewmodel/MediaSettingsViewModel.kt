package com.revolgenx.anilib.setting.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.common.data.store.AuthDataStore
import com.revolgenx.anilib.setting.data.store.MediaSettingsDataStore

class MediaSettingsViewModel(
    val authDataStore: AuthDataStore,
    val mediaSettingsDataStore: MediaSettingsDataStore
) : ViewModel() {

}