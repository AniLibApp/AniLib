package com.revolgenx.anilib.notification.data.source

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.source.BasePagingSource
import com.revolgenx.anilib.notification.data.field.NotificationField
import com.revolgenx.anilib.notification.data.service.NotificationService
import com.revolgenx.anilib.notification.ui.model.NotificationModel
import kotlinx.coroutines.flow.single

class NotificationPagingSource(
    field: NotificationField,
    private val notificationService: NotificationService
) :
    BasePagingSource<NotificationModel, NotificationField>(field) {

    override suspend fun loadPage(): PageModel<NotificationModel> {
        return notificationService.getNotificationList(field).single()
    }
}
