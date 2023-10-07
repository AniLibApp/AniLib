package com.revolgenx.anilib.notification.ui.viewmodel

import com.revolgenx.anilib.common.data.store.AuthDataStore
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.notification.data.field.NotificationField
import com.revolgenx.anilib.notification.data.service.NotificationService
import com.revolgenx.anilib.notification.data.source.NotificationPagingSource
import com.revolgenx.anilib.notification.ui.model.NotificationModel

class NotificationViewModel(
    private val notificationService: NotificationService,
    private val authDataStore: AuthDataStore
) :
    PagingViewModel<NotificationModel, NotificationField, NotificationPagingSource>(initialize = false) {

    override val field: NotificationField = NotificationField()

    init {
        launch {
            authDataStore.userId.collectNullable{ userId ->
                field.userId = userId
                refresh()
            }
        }
    }

    override val pagingSource: NotificationPagingSource
        get() = NotificationPagingSource(this.field, notificationService)
}