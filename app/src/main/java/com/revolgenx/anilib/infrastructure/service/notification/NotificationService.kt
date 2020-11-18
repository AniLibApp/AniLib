package com.revolgenx.anilib.infrastructure.service.notification

import com.revolgenx.anilib.data.field.notification.NotificationField
import com.revolgenx.anilib.data.model.notification.NotificationModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import io.reactivex.disposables.CompositeDisposable

interface NotificationService {
    fun getNotifications(field: NotificationField, compositeDisposable: CompositeDisposable, callback:((Resource<List<NotificationModel>>)->Unit))
}