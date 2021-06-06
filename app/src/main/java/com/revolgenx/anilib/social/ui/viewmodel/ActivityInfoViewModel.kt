package com.revolgenx.anilib.social.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.social.data.field.ActivityInfoField
import com.revolgenx.anilib.social.data.model.ActivityUnionModel
import com.revolgenx.anilib.social.infrastructure.service.ActivityUnionService
import com.revolgenx.anilib.ui.viewmodel.BaseViewModel

class ActivityInfoViewModel(private val service: ActivityUnionService) : BaseViewModel() {
    val field: ActivityInfoField = ActivityInfoField()
    val activityInfoLiveData = MutableLiveData<Resource<ActivityUnionModel>>()

    fun setActivityInfo(model: ActivityUnionModel) {
        activityInfoLiveData.value = Resource.success(model)
    }

    fun getActivityInfo() {
        activityInfoLiveData.value = Resource.loading(activityInfoLiveData.value?.data)
        service.getActivityInfo(field, compositeDisposable) {
            activityInfoLiveData.value = it
        }
    }
}