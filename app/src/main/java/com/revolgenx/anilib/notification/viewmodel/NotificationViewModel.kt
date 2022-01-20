package com.revolgenx.anilib.notification.viewmodel

import com.revolgenx.anilib.notification.service.NotificationService
import com.revolgenx.anilib.infrastructure.source.notification.NotificationSource
import com.revolgenx.anilib.common.viewmodel.SourceViewModel
import com.revolgenx.anilib.notification.data.field.NotificationField

class NotificationViewModel(private val notificationService: NotificationService) : SourceViewModel<NotificationSource, NotificationField>() {

    override var field: NotificationField = NotificationField()

    override fun createSource(): NotificationSource {
        source = NotificationSource(field, notificationService, compositeDisposable)
        return source!!
    }

}
