package com.revolgenx.anilib.notification.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.notification.data.field.NotificationField
import com.revolgenx.anilib.notification.data.service.NotificationService
import com.revolgenx.anilib.notification.data.source.NotificationPagingSource
import com.revolgenx.anilib.notification.ui.model.NotificationModel

class NotificationViewModel(
    private val notificationService: NotificationService,
    private val appPreferencesDataStore: AppPreferencesDataStore
) :
    PagingViewModel<NotificationModel, NotificationField?, NotificationPagingSource>() {

    override var field: NotificationField? by mutableStateOf(null)

    init {
        launch {
            appPreferencesDataStore.userId.collect{ userId ->
                field = NotificationField(userId = userId)
            }
        }
    }

    override val pagingSource: NotificationPagingSource
        get() = NotificationPagingSource(this.field!!, notificationService)
}