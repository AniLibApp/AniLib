package com.revolgenx.anilib.notification.data.service

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.common.ext.onIO
import com.revolgenx.anilib.notification.data.field.NotificationField
import com.revolgenx.anilib.notification.ui.model.NotificationModel
import com.revolgenx.anilib.notification.ui.model.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotificationServiceImpl(apolloRepository: ApolloRepository) :
    NotificationService, BaseService(apolloRepository) {

    override fun getNotificationList(field: NotificationField): Flow<PageModel<NotificationModel>> {
        val totalPage = field.page * field.perPage
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
                            it.unreadNotificationCount = data.User?.unreadNotificationCount ?: 0
                            field.includeNotificationCount = it.unreadNotificationCount > totalPage
                        }
                    }
                )
            }
        }.onIO()
    }
}