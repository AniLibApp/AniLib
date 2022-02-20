package com.revolgenx.anilib.app.setting.service

import com.revolgenx.anilib.app.setting.data.field.MediaListSettingField
import com.revolgenx.anilib.app.setting.data.field.MediaListSettingMutationField
import com.revolgenx.anilib.app.setting.data.field.MediaSettingField
import com.revolgenx.anilib.app.setting.data.field.MediaSettingMutateField
import com.revolgenx.anilib.app.setting.data.model.MediaListOptionModel
import com.revolgenx.anilib.app.setting.data.model.UserOptionsModel
import com.revolgenx.anilib.common.repository.network.BaseGraphRepository
import com.revolgenx.anilib.common.repository.network.converter.toModel
import com.revolgenx.anilib.common.repository.util.ERROR
import com.revolgenx.anilib.common.repository.util.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class SettingServiceImpl(private val baseGraphRepository: BaseGraphRepository) : SettingService {

    override fun getMediaSetting(
        field: MediaSettingField,
        compositeDisposable: CompositeDisposable,
        callback: (resource: Resource<UserOptionsModel>) -> Unit
    ) {
        val disposable = baseGraphRepository.request(field.toQueryOrMutation()).map {
            it.data?.user?.options?.userMediaOptions?.toModel()
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
            it.data?.user?.mediaListOptions?.let { mediaListOptions ->
                mediaListOptions.userMediaListOptions.let { option ->
                    option.toModel().also { model ->
                        model.animeList?.splitCompletedSectionByFormat =
                            mediaListOptions.animeList?.splitCompletedSectionByFormat
                        model.mangaList?.splitCompletedSectionByFormat =
                            mediaListOptions.mangaList?.splitCompletedSectionByFormat
                    }
                }
            }
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
        field: MediaListSettingMutationField,
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