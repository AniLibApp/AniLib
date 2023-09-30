package com.revolgenx.anilib.setting.data.service

import com.revolgenx.anilib.setting.data.field.MediaSettingsField
import com.revolgenx.anilib.setting.data.field.NotificationSettingsField
import com.revolgenx.anilib.setting.data.field.SaveMediaSettingsField
import com.revolgenx.anilib.setting.data.field.SaveNotificationSettingsField
import com.revolgenx.anilib.type.NotificationType
import com.revolgenx.anilib.user.ui.model.UserOptionsModel
import kotlinx.coroutines.flow.Flow

interface SettingsService {
    fun getNotificationSettings(field: NotificationSettingsField): Flow<Map<NotificationType, Boolean>?>
    fun saveNotificationSettings(field: SaveNotificationSettingsField): Flow<Boolean>
    fun getMediaSettings(field: MediaSettingsField): Flow<UserOptionsModel?>
    fun saveMediaSettings(field: SaveMediaSettingsField): Flow<Boolean>
}