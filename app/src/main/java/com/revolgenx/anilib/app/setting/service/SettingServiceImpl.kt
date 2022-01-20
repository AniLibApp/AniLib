package com.revolgenx.anilib.app.setting.service

import com.revolgenx.anilib.app.setting.data.field.MediaListSettingField
import com.revolgenx.anilib.app.setting.data.field.MediaListSettingMutateField
import com.revolgenx.anilib.app.setting.data.field.MediaSettingField
import com.revolgenx.anilib.app.setting.data.field.MediaSettingMutateField
import com.revolgenx.anilib.app.setting.data.model.MediaListOptionModel
import com.revolgenx.anilib.app.setting.data.model.UserOptionsModel
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.network.converter.toModel
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class SettingServiceImpl(private val baseGraphRepository: BaseGraphRepository) : SettingService {

    override fun getMediaSetting(
        field: MediaSettingField,
        compositeDisposable: CompositeDisposable,
        callback: (resource: Resource<UserOptionsModel>) -> Unit
    ) {
        val disposable = baseGraphRepository.request(field.toQueryOrMutation()).map {
            it.data()?.User()?.options()?.fragments()?.userMediaOptions()?.toModel()
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.invoke(Resource.success(it))
            }, {
                callback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })

        compositeDisposable.add(disposable)
    }

    override fun getMediaListSetting(
        field: MediaListSettingField,
        compositeDisposable: CompositeDisposable,
        callback: (resource: Resource<MediaListOptionModel>) -> Unit
    ) {
        val disposable = baseGraphRepository.request(field.toQueryOrMutation()).map {
            it.data()?.User()?.mediaListOptions()?.fragments()?.userMediaListOptions()?.toModel()
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.invoke(Resource.success(it))
            }, {
                callback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })
        compositeDisposable.add(disposable)
    }

    override fun saveMediaSetting(
        field: MediaSettingMutateField,
        compositeDisposable: CompositeDisposable,
        callback: ((status: Resource<Boolean>) -> Unit)
    ) {
        val disposable = baseGraphRepository.request(field.toQueryOrMutation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.invoke(Resource.success(true))
            }, {
                callback.invoke(Resource.error(it.message ?: ERROR, false, it))
            })

        compositeDisposable.add(disposable)
    }

    override fun saveMediaListSetting(
        field: MediaListSettingMutateField,
        compositeDisposable: CompositeDisposable,
        callback: (status: Resource<Boolean>) -> Unit
    ) {
        val disposable = baseGraphRepository.request(field.toQueryOrMutation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.invoke(Resource.success(true))
            }, {
                callback.invoke(Resource.error(it.message ?: ERROR, false, it))
            })
        compositeDisposable.add(disposable)
    }
}