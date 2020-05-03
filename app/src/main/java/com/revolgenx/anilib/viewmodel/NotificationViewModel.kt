package com.revolgenx.anilib.viewmodel

import com.revolgenx.anilib.field.notification.NotificationField
import com.revolgenx.anilib.service.notification.NotificationService
import com.revolgenx.anilib.source.notification.NotificationSource

class NotificationViewModel(private val notificationService: NotificationService) : SourceViewModel<NotificationSource, NotificationField>() {

    val field = NotificationField()

    override fun createSource(field: NotificationField): NotificationSource {
        source = NotificationSource(field, notificationService, compositeDisposable)
        return source!!
    }

}
