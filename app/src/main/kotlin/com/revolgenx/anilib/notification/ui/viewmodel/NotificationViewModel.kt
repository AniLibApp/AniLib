package com.revolgenx.anilib.notification.ui.viewmodel

import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.notification.data.field.NotificationField
import com.revolgenx.anilib.notification.data.service.NotificationService
import com.revolgenx.anilib.notification.data.source.NotificationPagingSource
import com.revolgenx.anilib.notification.ui.model.NotificationModel

class NotificationViewModel(
    private val notificationService: NotificationService,
    appPreferencesDataStore: AppPreferencesDataStore
) :
    PagingViewModel<NotificationModel, NotificationField?, NotificationPagingSource>() {

    override var field: NotificationField = NotificationField(userId = appPreferencesDataStore.userId.get())

    override val pagingSource: NotificationPagingSource
        get() = NotificationPagingSource(this.field, notificationService)


}