package com.revolgenx.anilib.app.setting.service

import com.revolgenx.anilib.app.setting.data.field.MediaListSettingField
import com.revolgenx.anilib.app.setting.data.field.MediaListSettingMutateField
import com.revolgenx.anilib.app.setting.data.field.MediaSettingField
import com.revolgenx.anilib.app.setting.data.field.MediaSettingMutateField
import com.revolgenx.anilib.app.setting.data.model.MediaListOptionModel
import com.revolgenx.anilib.app.setting.data.model.UserOptionsModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import io.reactivex.disposables.CompositeDisposable

interface SettingService {
    fun getMediaSetting(
        field: MediaSettingField,
        compositeDisposable: CompositeDisposable,
        callback: (resource: Resource<UserOptionsModel>) -> Unit
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