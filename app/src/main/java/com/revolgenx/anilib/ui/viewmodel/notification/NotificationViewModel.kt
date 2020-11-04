package com.revolgenx.anilib.ui.viewmodel.notification

import com.revolgenx.anilib.data.field.notification.NotificationField
import com.revolgenx.anilib.infrastructure.service.notification.NotificationService
import com.revolgenx.anilib.infrastructure.source.notification.NotificationSource
import com.revolgenx.anilib.ui.viewmodel.SourceViewModel

class NotificationViewModel(private val notificationService: NotificationService) : SourceViewModel<NotificationSource, NotificationField>() {

    override var field: NotificationField = NotificationField()

    override fun createSource(): NotificationSource {
        source = NotificationSource(field, notificationService, compositeDisposable)
        return source!!
    }

}
