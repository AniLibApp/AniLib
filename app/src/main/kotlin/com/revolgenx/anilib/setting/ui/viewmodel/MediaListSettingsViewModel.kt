package com.revolgenx.anilib.setting.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.revolgenx.anilib.common.data.store.AuthPreferencesDataStore
import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.setting.data.field.MediaListSettingsField
import com.revolgenx.anilib.setting.data.field.SaveMediaListSettingsField
import com.revolgenx.anilib.setting.data.service.SettingsService
import com.revolgenx.anilib.setting.ui.model.MediaListSettingsModel
import com.revolgenx.anilib.user.ui.model.toMediaTitleType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

class MediaListSettingsViewModel(
    val authPreferencesDataStore: AuthPreferencesDataStore,
    private val settingsService: SettingsService
) :
    ResourceViewModel<MediaListSettingsModel, MediaListSettingsField>() {
    override val field: MediaListSettingsField = MediaListSettingsField().also {
        it.userId = authPreferencesDataStore.userId.get()
    }

    override fun load(): Flow<MediaListSettingsModel?> {
        return settingsService.getMediaListSettings(field)
    }

    override fun save() {
        getData()?.let { data ->
            super.save()

            val saveField = SaveMediaListSettingsField(
                scoreFormat = data.scoreFormat.value,
                advancedScoring = data.advancedScoring.toList(),
                rowOrder = data.rowOrder.value,
                animeCustomLists = data.animeCustomLists.toList(),
                mangaCustomLists = data.mangaCustomLists.toList(),
                advancedScoringEnabled = data.advancedScoringEnabled.value,
                splitCompletedAnimeListByFormat = data.splitCompletedAnimeSectionByFormat.value,
                splitCompletedMangaListByFormat = data.splitCompletedMangaSectionByFormat.value
            )

            settingsService.saveMediaListSettings(saveField).onEach {
                saveComplete(it)
            }.catch {
                saveFailed(it)
            }.launchIn(viewModelScope)
        }
    }
}