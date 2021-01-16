package com.revolgenx.anilib.infrastructure.service.setting

import com.revolgenx.anilib.data.field.setting.MediaListSettingField
import com.revolgenx.anilib.data.field.setting.MediaListSettingMutateField
import com.revolgenx.anilib.data.field.setting.MediaSettingField
import com.revolgenx.anilib.data.field.setting.MediaSettingMutateField
import com.revolgenx.anilib.data.model.setting.MediaListOptionModel
import com.revolgenx.anilib.data.model.setting.MediaOptionModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import io.reactivex.disposables.CompositeDisposable

interface SettingService {
    fun getMediaSetting(
        field: MediaSettingField,
        compositeDisposable: CompositeDisposable,
        callback: (resource: Resource<MediaOptionModel>) -> Unit
    )

    fun getMediaListSetting(
        field: MediaListSettingField,
        compositeDisposable: CompositeDisposable,
        callback: (resource: Resource<MediaListOptionModel>) -> Unit
    )

    fun saveMediaSetting(
        field: MediaSettingMutateField,
        compositeDisposable: CompositeDisposable,
        callback: ((status: Resource<Boolean>) -> Unit)
    )

    fun saveMediaListSetting(
        field: MediaListSettingMutateField,
        compositeDisposable: CompositeDisposable,
        callback: ((status: Resource<Boolean>) -> Unit)
    )
}