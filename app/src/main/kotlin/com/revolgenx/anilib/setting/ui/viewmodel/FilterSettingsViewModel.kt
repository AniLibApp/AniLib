package com.revolgenx.anilib.setting.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore

class FilterSettingsViewModel(
    appPreferencesDataStore: AppPreferencesDataStore
): ViewModel() {
    val maxEpisodesPref = appPreferencesDataStore.maxEpisodes
    val maxDurationsPref = appPreferencesDataStore.maxDuration
    val maxChaptersPref = appPreferencesDataStore.maxChapters
    val maxVolumesPref = appPreferencesDataStore.maxVolumes
}