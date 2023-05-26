package com.revolgenx.anilib.notification.ui.viewmodel

import com.revolgenx.anilib.common.data.store.AppDataStore
import com.revolgenx.anilib.common.data.store.userId
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ui.screen.PagingViewModel
import com.revolgenx.anilib.notification.data.field.NotificationField
import com.revolgenx.anilib.notification.data.service.NotificationService
import com.revolgenx.anilib.notification.data.source.NotificationPagingSource
import com.revolgenx.anilib.notification.ui.model.NotificationModel
import kotlinx.coroutines.flow.first

class NotificationViewModel(
    private val notificationService: NotificationService,
    private val appDataStore: AppDataStore
) :
    PagingViewModel<NotificationModel, NotificationField, NotificationPagingSource>(initialize = false) {

    override val field: NotificationField = NotificationField()

    init {
        launch {
            field.userId = appDataStore.userId().first()
            refresh()
        }
    }

    override val pagingSource: NotificationPagingSource
        get() = NotificationPagingSource(this.field, notificationService)
}