package com.revolgenx.anilib.notification.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.common.viewmodel.BaseViewModel

class NotificationStoreViewModel : BaseViewModel() {
    private val unreadNotification: MutableLiveData<Int> = MutableLiveData(0)
    var unreadNotificationCount: LiveData<Int> = unreadNotification

    fun setUnreadNotificationCount(count:Int){
        unreadNotification.value = count
    }
}