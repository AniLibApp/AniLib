package com.revolgenx.anilib.setting.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.revolgenx.anilib.common.data.state.ResourceState
import com.revolgenx.anilib.common.data.store.AuthDataStore
import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.setting.data.field.MediaSettingsField
import com.revolgenx.anilib.setting.data.field.SaveMediaSettingsField
import com.revolgenx.anilib.setting.data.service.SettingsService
import com.revolgenx.anilib.setting.data.store.MediaSettingsDataStore
import com.revolgenx.anilib.type.UserTitleLanguage
import com.revolgenx.anilib.user.ui.model.UserOptionsModel
import com.revolgenx.anilib.user.ui.model.UserOptionsUiModel
import com.revolgenx.anilib.user.ui.model.toUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class MediaSettingsViewModel(
    val authDataStore: AuthDataStore,
    val mediaSettingsDataStore: MediaSettingsDataStore,
    private val settingsService: SettingsService
) : ResourceViewModel<UserOptionsUiModel, MediaSettingsField>() {

    override val field: MediaSettingsField = MediaSettingsField().also {
        it.userId = authDataStore.userId.getNullable()
    }

    override fun load(): Flow<UserOptionsUiModel?> {
        return settingsService.getMediaSettings(field).map { it?.toUiModel() }
    }

    override fun save() {
        resource.value?.stateValue?.let { userOptionsUiModel ->
            saveResource.value = ResourceState.loading()
            val titleLanguage = userOptionsUiModel.titleLanguage.value
            val airingNotifications = userOptionsUiModel.airingNotifications.value
            val saveField = SaveMediaSettingsField(
                model = UserOptionsModel(
                    titleLanguage = UserTitleLanguage.values()[titleLanguage],
                    airingNotifications = airingNotifications,
                    activityMergeTime = userOptionsUiModel.activityMergeTime.intValue
                )
            )
            settingsService.saveMediaSettings(saveField).onEach {
                saveResource.value = ResourceState.success(it)
            }.catch {
                saveResource.value = ResourceState.error(it)
            }
                .launchIn(viewModelScope)
        }
    }

}