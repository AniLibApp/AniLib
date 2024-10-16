package com.revolgenx.anilib.setting.data.service

import androidx.compose.runtime.mutableStateOf
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.setting.data.field.MediaListSettingsField
import com.revolgenx.anilib.setting.data.field.MediaSettingsField
import com.revolgenx.anilib.setting.data.field.NotificationSettingsField
import com.revolgenx.anilib.setting.data.field.SaveMediaListSettingsField
import com.revolgenx.anilib.setting.data.field.SaveMediaSettingsField
import com.revolgenx.anilib.setting.data.field.SaveNotificationSettingsField
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.setting.ui.model.MediaListSettingsModel
import com.revolgenx.anilib.setting.ui.model.MediaSettingsModel
import com.revolgenx.anilib.setting.ui.model.toModel
import com.revolgenx.anilib.type.NotificationType
import com.revolgenx.anilib.user.ui.model.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsServiceImpl(
    apolloRepository: ApolloRepository,
    appPreferencesDataStore: AppPreferencesDataStore
) : BaseService(apolloRepository, appPreferencesDataStore),
    SettingsService {
    override fun getNotificationSettings(field: NotificationSettingsField): Flow<Map<NotificationType, Boolean>?> {
        return field.toQuery().mapData {
            it.data?.user?.options?.notificationOptions?.filterNotNull()
                ?.associateBy({ it.type!! }, { it.enabled == true })
        }
    }

    override fun saveNotificationSettings(field: SaveNotificationSettingsField): Flow<Boolean> {
        return field.toMutation().mapData { it.dataAssertNoErrors.updateUser?.id != null }
    }

    override fun getMediaSettings(field: MediaSettingsField): Flow<MediaSettingsModel?> {
        return field.toQuery()
            .mapData {
                it.dataAssertNoErrors.user?.options?.userMediaOptions?.toModel()?.let {
                    MediaSettingsModel(
                        mutableStateOf(it)
                    )
                }
            }
    }

    override fun saveMediaSettings(field: SaveMediaSettingsField): Flow<Boolean> {
        return field.toMutation().mapData { it.dataAssertNoErrors.updateUser?.id != null }
    }

    override fun getMediaListSettings(field: MediaListSettingsField): Flow<MediaListSettingsModel?> {
        return field.toQuery()
            .mapData {
                it.dataAssertNoErrors.user?.mediaListOptions?.userMediaListOptions?.toModel()?.toModel()
            }
    }

    override fun saveMediaListSettings(field: SaveMediaListSettingsField): Flow<Boolean> {
        return field.toMutation().mapData { it.dataAssertNoErrors.updateUser?.id != null }
    }
}