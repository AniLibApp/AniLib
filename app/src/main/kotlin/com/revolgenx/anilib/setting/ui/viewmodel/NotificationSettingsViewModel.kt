package com.revolgenx.anilib.setting.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.revolgenx.anilib.common.data.state.ResourceState
import com.revolgenx.anilib.common.data.store.AuthDataStore
import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.setting.data.field.UserNotificationSettingsField
import com.revolgenx.anilib.setting.data.service.SettingsService
import com.revolgenx.anilib.type.NotificationType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

class NotificationSettingsViewModel(
    authDataStore: AuthDataStore,
    private val settingsService: SettingsService
) : ResourceViewModel<Map<NotificationType, MutableState<Boolean>>, UserNotificationSettingsField>() {
    override val field: UserNotificationSettingsField = UserNotificationSettingsField()

    init {
        field.userId = authDataStore.userId.get()
        getResource()
    }

    val loading = mutableStateOf(false)
    val saveResource: MutableState<ResourceState<Boolean>?> = mutableStateOf(null)
    override fun loadData(): Flow<Map<NotificationType, MutableState<Boolean>>?> {
        return settingsService.getNotificationSettings(field)
    }

    fun save() {
        resource.value?.stateValue?.let {
            saveResource.value = ResourceState.loading()
            loading.value = true
            settingsService.saveNotificationSettings(
                SaveUserNotificationSettingsField(it.mapValues { it.value.value })
            )
                .onEach {
                    saveResource.value = ResourceState.success(it)
                }.catch {
                    saveResource.value = ResourceState.error(it)
                }.onCompletion {
                    loading.value = false
                }
                .launchIn(viewModelScope)
        }
    }
}