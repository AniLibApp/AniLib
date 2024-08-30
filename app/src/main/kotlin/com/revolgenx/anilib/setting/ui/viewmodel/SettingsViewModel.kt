package com.revolgenx.anilib.setting.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore

class SettingsViewModel(
    val appPreferencesDataStore: AppPreferencesDataStore
) : ViewModel() {
    val bugReport = appPreferencesDataStore.bugReport
}