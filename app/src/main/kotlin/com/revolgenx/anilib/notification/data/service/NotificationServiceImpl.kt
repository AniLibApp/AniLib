package com.revolgenx.anilib.notification.data.service

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.common.ext.onIO
import com.revolgenx.anilib.notification.data.field.NotificationField
import com.revolgenx.anilib.notification.ui.model.NotificationModel
import com.revolgenx.anilib.notification.ui.model.toModel
import com.revolgenx.anilib.setting.data.store.MediaSettingsDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotificationServiceImpl(
    apolloRepository: ApolloRepository,
    mediaSettingsDataStore: MediaSettingsDataStore
) :
    NotificationService, BaseService(apolloRepository, mediaSettingsDataStore) {

    override fun getNotificationList(field: NotificationField): Flow<PageModel<NotificationModel>> {
        if (field.page == 1) {
            field.includeNotificationCount = true
        }
        return field.toQuery().map {
            val data = it.dataAssertNoErrors
            data.page.let { page ->
                PageModel(
                    pageInfo = page.pageInfo.pageInfo,
                    data = page.notifications?.mapNotNull { notification ->
                        notification?.toModel()?.also {
                            field.unreadNotificationCount =
                                field.unreadNotificationCount ?: data.User?.unreadNotificationCount
                            it.unreadNotificationCount = field.unreadNotificationCount ?: 0
                            field.includeNotificationCount = false
                        }
                    }
                )
            }
        }.onIO()
    }
}