package com.revolgenx.anilib.activity.viewmodel

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.common.viewmodel.BaseViewModel

class MainSharedVM : BaseViewModel() {
    var mediaListCurrentTab = MutableLiveData<Int?>()
    var listNavigateToTopListener: (() -> Unit)? = null
    var activityNavigateToTopListener: (() -> Unit)? = null
}