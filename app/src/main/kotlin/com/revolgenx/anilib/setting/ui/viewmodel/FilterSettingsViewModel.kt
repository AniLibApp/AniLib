package com.revolgenx.anilib.setting.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revolgenx.anilib.common.data.state.ResourceState
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.setting.data.service.SettingsService
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class FilterSettingsViewModel(
    appPreferencesDataStore: AppPreferencesDataStore,
    private val settingsService: SettingsService
) : ViewModel() {
    val maxEpisodesPref = appPreferencesDataStore.maxEpisodes
    val maxDurationsPref = appPreferencesDataStore.maxDuration
    val maxChaptersPref = appPreferencesDataStore.maxChapters
    val maxVolumesPref = appPreferencesDataStore.maxVolumes

    val refreshTagsState = mutableStateOf<ResourceState<Unit>?>(null)

    fun refreshTagsAndGenre() {
        refreshTagsState.value = ResourceState.loading()
        settingsService
            .refreshTagsAndGenreCollection()
            .onEach {
                refreshTagsState.value = ResourceState.success(null)
            }
            .catch {
                refreshTagsState.value = ResourceState.error(it)
            }.launchIn(viewModelScope)
    }
}