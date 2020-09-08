package com.revolgenx.anilib.viewmodel.notification

import com.revolgenx.anilib.field.notification.NotificationField
import com.revolgenx.anilib.service.notification.NotificationService
import com.revolgenx.anilib.source.notification.NotificationSource
import com.revolgenx.anilib.viewmodel.SourceViewModel

class NotificationViewModel(private val notificationService: NotificationService) : SourceViewModel<NotificationSource, NotificationField>() {

    override var field: NotificationField = NotificationField()

    override fun createSource(): NotificationSource {
        source = NotificationSource(field, notificationService, compositeDisposable)
        return source!!
    }

}
