package com.revolgenx.anilib.setting.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.common.data.store.AuthPreferencesDataStore

class SettingsViewModel(
    val authPreferencesDataStore: AuthPreferencesDataStore
) : ViewModel() {
}