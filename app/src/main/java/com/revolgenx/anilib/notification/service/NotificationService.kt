package com.revolgenx.anilib.notification.service

import com.revolgenx.anilib.notification.data.model.NotificationModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.notification.data.field.NotificationField
import com.revolgenx.anilib.notification.data.field.UserNotificationMutateField
import com.revolgenx.anilib.notification.data.field.UserNotificationSettingField
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
        field: UserNotificationSettingField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<Map<NotificationType, Boolean>>) -> Unit
    )
}