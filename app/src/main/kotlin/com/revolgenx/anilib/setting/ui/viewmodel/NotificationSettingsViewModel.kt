package com.revolgenx.anilib.setting.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.revolgenx.anilib.common.data.state.ResourceState
import com.revolgenx.anilib.common.data.store.AuthDataStore
import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.setting.data.field.NotificationSettingsField
import com.revolgenx.anilib.setting.data.field.SaveNotificationSettingsField
import com.revolgenx.anilib.setting.data.service.SettingsService
import com.revolgenx.anilib.type.NotificationType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

class NotificationSettingsViewModel(
    authDataStore: AuthDataStore,
    private val settingsService: SettingsService
) : ResourceViewModel<Map<NotificationType, MutableState<Boolean>>, NotificationSettingsField>(){
    override val field: NotificationSettingsField = NotificationSettingsField().also {
        it.userId = authDataStore.userId.get()
    }

    init {
        getResource()
    }

    override val saveResource: MutableState<ResourceState<Any>?> = mutableStateOf(null)
    override fun load(): Flow<Map<NotificationType, MutableState<Boolean>>?> {
        return settingsService.getNotificationSettings(field)
            .map { it?.mapValues { mutableStateOf(it.value) } }
    }

    override fun save() {
        resource.value?.stateValue?.let {
            saveResource.value = ResourceState.loading()
            settingsService.saveNotificationSettings(
                SaveNotificationSettingsField(it.mapValues { it.value.value })
            )
                .onEach {
                    saveResource.value = ResourceState.success(it)
                }.catch {
                    saveResource.value = ResourceState.error(it)
                }
                .launchIn(viewModelScope)
        }
    }
}