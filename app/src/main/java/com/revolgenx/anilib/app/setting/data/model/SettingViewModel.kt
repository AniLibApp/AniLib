package com.revolgenx.anilib.app.setting.data.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.common.preference.getStoredMediaListOptions
import com.revolgenx.anilib.common.preference.getStoredMediaOptions
import com.revolgenx.anilib.common.preference.storeMediaListOptions
import com.revolgenx.anilib.common.preference.storeMediaOption
import com.revolgenx.anilib.app.setting.data.field.MediaListSettingField
import com.revolgenx.anilib.app.setting.data.field.MediaListSettingMutateField
import com.revolgenx.anilib.app.setting.data.field.MediaSettingField
import com.revolgenx.anilib.app.setting.data.field.MediaSettingMutateField
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.app.setting.service.SettingService
import com.revolgenx.anilib.common.viewmodel.BaseViewModel

class SettingViewModel(private val settingService: SettingService) : BaseViewModel() {

    val mediaOptionLiveData: MutableLiveData<Resource<UserOptionsModel>> =
        MutableLiveData()
    val mediaListSettingLiveData: MutableLiveData<Resource<MediaListOptionModel>> =
        MutableLiveData()

    val mediaSettingField = MediaSettingField()
    val mediaListSettingField = MediaListSettingField()

    fun getMediaSetting(context: Context) {
        mediaOptionLiveData.value = Resource.loading(getStoredMediaOptions(context))
        settingService.getMediaSetting(mediaSettingField, compositeDisposable) {
            if (it.status == Status.SUCCESS) {
                storeMediaOption(context, it.data)
            }

            mediaOptionLiveData.value = it
        }
    }

    fun getListSetting(context: Context) {
        mediaListSettingLiveData.value = Resource.loading(getStoredMediaListOptions(context))
        settingService.getMediaListSetting(mediaListSettingField, compositeDisposable) {
            if (it.status == Status.SUCCESS) {
                storeMediaListOptions(context, it.data)
            }
            mediaListSettingLiveData.value = it
        }
    }

    fun setMediaSetting(
        context: Context,
        field: MediaSettingMutateField
    ): LiveData<Resource<Boolean>> {
        val liveData = MutableLiveData<Resource<Boolean>>()
        liveData.value = Resource.loading()
        settingService.saveMediaSetting(field, compositeDisposable) {
            if (it.status == Status.SUCCESS) {
                storeMediaOption(context, field.model)
            }
            liveData.value = it
        }
        return liveData
    }

    fun setMediaListSetting(
        context: Context,
        field: MediaListSettingMutateField
    ): MutableLiveData<Resource<Boolean>> {
        val liveData = MutableLiveData<Resource<Boolean>>()
        liveData.value = Resource.loading()
        settingService.saveMediaListSetting(field, compositeDisposable) {
            if (it.status == Status.SUCCESS) {
                storeMediaListOptions(context, field.model)
            }
            liveData.value = it
        }
        return liveData
    }


    fun getNotificationSetting() {

    }


    fun setNotificationSetting() {

    }

}