package com.revolgenx.anilib.setting.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.revolgenx.anilib.common.data.state.ResourceState
import com.revolgenx.anilib.common.data.store.AuthPreferencesDataStore
import com.revolgenx.anilib.common.data.store.GeneralPreferencesDataStore
import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.setting.data.field.NotificationSettingsField
import com.revolgenx.anilib.setting.data.field.SaveNotificationSettingsField
import com.revolgenx.anilib.setting.data.service.SettingsService
import com.revolgenx.anilib.type.NotificationType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class NotificationSettingsViewModel(
    authPreferencesDataStore: AuthPreferencesDataStore,
    val generalPreferencesDataStore: GeneralPreferencesDataStore,
    private val settingsService: SettingsService
) : ResourceViewModel<Map<NotificationType, MutableState<Boolean>>, NotificationSettingsField>(){
    override val field: NotificationSettingsField = NotificationSettingsField().also {
        it.userId = authPreferencesDataStore.userId.get()
    }

    init {
        getResource()
    }

    override fun load(): Flow<Map<NotificationType, MutableState<Boolean>>?> {
        return settingsService.getNotificationSettings(field)
            .map { it?.mapValues { mutableStateOf(it.value) } }
    }

    override fun save() {
        resource.value?.stateValue?.let {
            super.save()
            settingsService.saveNotificationSettings(
                SaveNotificationSettingsField(it.mapValues { it.value.value })
            )
                .onEach {
                    saveComplete(it)
                }.catch {
                    saveFailed(it)
                }
                .launchIn(viewModelScope)
        }
    }
}