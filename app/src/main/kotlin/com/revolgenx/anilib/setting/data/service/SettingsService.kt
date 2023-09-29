package com.revolgenx.anilib.setting.data.service

import androidx.compose.runtime.MutableState
import com.revolgenx.anilib.setting.data.field.UserNotificationSettingsField
import com.revolgenx.anilib.setting.ui.viewmodel.SaveUserNotificationSettingsField
import com.revolgenx.anilib.type.NotificationType
import kotlinx.coroutines.flow.Flow

interface SettingsService {
    fun getNotificationSettings(field: UserNotificationSettingsField): Flow<Map<NotificationType, MutableState<Boolean>>?>
    fun saveNotificationSettings(field: SaveUserNotificationSettingsField): Flow<Boolean>

}