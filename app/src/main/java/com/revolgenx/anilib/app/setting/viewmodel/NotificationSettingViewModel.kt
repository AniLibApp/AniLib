package com.revolgenx.anilib.app.setting.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.notification.service.NotificationService
import com.revolgenx.anilib.type.NotificationType
import com.revolgenx.anilib.common.viewmodel.BaseViewModel
import com.revolgenx.anilib.notification.data.field.UserNotificationMutateField
import com.revolgenx.anilib.notification.data.field.UserNotificationSettingField

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