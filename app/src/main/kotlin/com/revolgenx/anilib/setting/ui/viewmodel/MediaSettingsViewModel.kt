package com.revolgenx.anilib.setting.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.revolgenx.anilib.common.data.store.AuthPreferencesDataStore
import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.setting.data.field.MediaSettingsField
import com.revolgenx.anilib.setting.data.field.SaveMediaSettingsField
import com.revolgenx.anilib.setting.data.service.SettingsService
import com.revolgenx.anilib.setting.data.store.MediaSettingsPreferencesDataStore
import com.revolgenx.anilib.setting.ui.model.MediaSettingsModel
import com.revolgenx.anilib.user.ui.model.UserOptionsModel
import com.revolgenx.anilib.user.ui.model.toMediaTitleType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

class MediaSettingsViewModel(
    val authPreferencesDataStore: AuthPreferencesDataStore,
    val mediaSettingsPreferencesDataStore: MediaSettingsPreferencesDataStore,
    private val settingsService: SettingsService
) : ResourceViewModel<MediaSettingsModel, MediaSettingsField>() {

    override val field: MediaSettingsField = MediaSettingsField().also {
        it.userId = authPreferencesDataStore.userId.get()
    }

    override fun load(): Flow<MediaSettingsModel?> {
        return settingsService.getMediaSettings(field)
    }

    override fun save() {
        getData()?.options?.value?.let { userOptionsUiModel ->
            super.save()
            val titleLanguage = userOptionsUiModel.titleLanguage
            val airingNotifications = userOptionsUiModel.airingNotifications
            val saveField = SaveMediaSettingsField(
                model = UserOptionsModel(
                    titleLanguage = titleLanguage,
                    airingNotifications = airingNotifications,
                    activityMergeTime = userOptionsUiModel.activityMergeTime
                )
            )
            settingsService.saveMediaSettings(saveField).onEach {
                saveComplete(it)
            }.catch {
                saveFailed(it)
            }.onCompletion {
                if(it == null){
                    mediaSettingsPreferencesDataStore.mediaTitleType.set(titleLanguage.toMediaTitleType())
                }
            }.launchIn(viewModelScope)
        }
    }

}