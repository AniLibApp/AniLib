package com.revolgenx.anilib.setting.data.service

import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.common.ext.onIO
import com.revolgenx.anilib.setting.data.field.MediaSettingsField
import com.revolgenx.anilib.setting.data.field.NotificationSettingsField
import com.revolgenx.anilib.setting.data.field.SaveMediaSettingsField
import com.revolgenx.anilib.setting.data.field.SaveNotificationSettingsField
import com.revolgenx.anilib.type.NotificationType
import com.revolgenx.anilib.user.ui.model.UserOptionsModel
import com.revolgenx.anilib.user.ui.model.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsServiceImpl(apolloRepository: ApolloRepository) : BaseService(apolloRepository),
    SettingsService {
    override fun getNotificationSettings(field: NotificationSettingsField): Flow<Map<NotificationType, Boolean>?> {
        return field.toQuery().map {
            it.data?.user?.options?.notificationOptions?.filterNotNull()
                ?.associateBy({ it.type!! }, { it.enabled == true })
        }.onIO()
    }

    override fun saveNotificationSettings(field: SaveNotificationSettingsField): Flow<Boolean> {
        return field.toMutation().map { it.dataAssertNoErrors.updateUser?.id != null }.onIO()
    }

    override fun getMediaSettings(field: MediaSettingsField): Flow<UserOptionsModel?> {
        return field.toQuery().map { it.dataAssertNoErrors.user?.options?.userMediaOptions?.toModel() }.onIO()
    }

    override fun saveMediaSettings(field: SaveMediaSettingsField): Flow<Boolean> {
        return field.toMutation().map { it.dataAssertNoErrors.updateUser?.id != null }.onIO()
    }
}