package com.revolgenx.anilib.ui.viewmodel.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.data.field.notification.UserNotificationMutateField
import com.revolgenx.anilib.data.field.notification.UserNotificationSettingField
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.service.notification.NotificationService
import com.revolgenx.anilib.type.NotificationType
import com.revolgenx.anilib.ui.viewmodel.BaseViewModel

class NotificationSettingViewModel(private val notificationService: NotificationService) :
    BaseViewModel() {

    val field: UserNotificationSettingField = UserNotificationSettingField()
    val notificationSettings = MutableLiveData<Resource<Map<NotificationType, Boolean>>>()

    fun getNotificationSettings() {
        notificationSettings.value = Resource.loading(null)
        notificationService.getNotificationSettings(field, compositeDisposable) {
            notificationSettings.value = it
        }
    }

    fun saveNotificationSetting(field: UserNotificationMutateField): LiveData<Resource<Boolean>> {
        val saveNotifSettingLiveData = MutableLiveData<Resource<Boolean>>()
        saveNotifSettingLiveData.value = Resource.loading(null)
        notificationService.saveNotificationSettings(field, compositeDisposable) {
            saveNotifSettingLiveData.value = it
        }
        return saveNotifSettingLiveData
    }
}
