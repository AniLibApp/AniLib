package com.revolgenx.anilib.service.notification

import com.revolgenx.anilib.field.notification.NotificationField
import com.revolgenx.anilib.model.notification.NotificationModel
import com.revolgenx.anilib.repository.util.Resource
import io.reactivex.disposables.CompositeDisposable

interface NotificationService {
    fun getNotifications(field: NotificationField, compositeDisposable: CompositeDisposable, callback:((Resource<List<NotificationModel>>)->Unit))
}