package com.revolgenx.anilib.ui.viewmodel.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.ui.viewmodel.BaseViewModel

class NotificationStoreViewModel : BaseViewModel() {
    private val unreadNotification:MutableLiveData<Int> = MutableLiveData(0)
    var unreadNotificationCount:LiveData<Int> = unreadNotification

    fun setUnreadNotificationCount(count:Int){
        unreadNotification.value = count
    }
}