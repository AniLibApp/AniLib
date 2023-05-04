package com.revolgenx.anilib.notification.data.service

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.notification.data.field.NotificationField
import com.revolgenx.anilib.notification.ui.model.NotificationModel
import kotlinx.coroutines.flow.Flow

interface NotificationService {
    fun getNotificationList(field: NotificationField): Flow<PageModel<NotificationModel>>
}