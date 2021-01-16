package com.revolgenx.anilib.infrastructure.service.notification

import com.revolgenx.anilib.data.field.notification.NotificationField
import com.revolgenx.anilib.data.field.notification.UserNotificationMutateField
import com.revolgenx.anilib.data.field.notification.UserNotificationSettingField
import com.revolgenx.anilib.data.model.notification.NotificationModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.type.NotificationType
import io.reactivex.disposables.CompositeDisposable

interface NotificationService {
    fun getNotifications(
        field: NotificationField,
        compositeDisposable: CompositeDisposable,
        callback: ((Resource<List<NotificationModel>>) -> Unit)
    )

    fun saveNotificationSettings(
        field: UserNotificationMutateField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<Boolean>) -> Unit
    )

    fun getNotificationSettings(
        field:UserNotificationSettingField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<Map<NotificationType, Boolean>>) -> Unit
    )
}