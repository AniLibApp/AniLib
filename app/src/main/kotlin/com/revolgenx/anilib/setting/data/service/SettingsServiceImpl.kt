package com.revolgenx.anilib.setting.data.service

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.common.ext.onIO
import com.revolgenx.anilib.setting.data.field.UserNotificationSettingsField
import com.revolgenx.anilib.setting.ui.viewmodel.SaveUserNotificationSettingsField
import com.revolgenx.anilib.type.NotificationType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsServiceImpl(apolloRepository: ApolloRepository) : BaseService(apolloRepository),
    SettingsService {
    override fun getNotificationSettings(field: UserNotificationSettingsField): Flow<Map<NotificationType, MutableState<Boolean>>?> {
        return field.toQuery().map {
            it.data?.user?.options?.notificationOptions?.filterNotNull()
                ?.associateBy({ it.type!! }, { mutableStateOf(it.enabled == true) })
        }.onIO()
    }

    override fun saveNotificationSettings(field: SaveUserNotificationSettingsField): Flow<Boolean> {
        return field.toMutation().map { it.dataAssertNoErrors.updateUser?.id != null }.onIO()
    }
}