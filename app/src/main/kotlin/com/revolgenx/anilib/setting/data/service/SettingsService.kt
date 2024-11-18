package com.revolgenx.anilib.setting.data.service

import com.revolgenx.anilib.setting.data.field.MediaListSettingsField
import com.revolgenx.anilib.setting.data.field.MediaSettingsField
import com.revolgenx.anilib.setting.data.field.NotificationSettingsField
import com.revolgenx.anilib.setting.data.field.SaveMediaListSettingsField
import com.revolgenx.anilib.setting.data.field.SaveMediaSettingsField
import com.revolgenx.anilib.setting.data.field.SaveNotificationSettingsField
import com.revolgenx.anilib.setting.ui.model.MediaListSettingsModel
import com.revolgenx.anilib.setting.ui.model.MediaSettingsModel
import com.revolgenx.anilib.type.NotificationType
import com.revolgenx.anilib.user.ui.model.MediaListOptionModel
import com.revolgenx.anilib.user.ui.model.UserOptionsModel
import kotlinx.coroutines.flow.Flow

interface SettingsService {
    fun getNotificationSettings(field: NotificationSettingsField): Flow<Map<NotificationType, Boolean>?>
    fun saveNotificationSettings(field: SaveNotificationSettingsField): Flow<Boolean>
    fun getMediaSettings(field: MediaSettingsField): Flow<MediaSettingsModel?>
    fun saveMediaSettings(field: SaveMediaSettingsField): Flow<Boolean>
    fun getMediaListSettings(field: MediaListSettingsField): Flow<MediaListSettingsModel?>
    fun saveMediaListSettings(field: SaveMediaListSettingsField): Flow<Boolean>
    fun refreshTagsAndGenreCollection(): Flow<Unit>
}