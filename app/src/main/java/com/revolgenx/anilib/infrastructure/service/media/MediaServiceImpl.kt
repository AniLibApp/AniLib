package com.revolgenx.anilib.infrastructure.service.media

import com.revolgenx.anilib.data.field.media.MediaField
import com.revolgenx.anilib.data.model.CommonMediaModel
import com.revolgenx.anilib.data.model.home.SelectableCommonMediaModel
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.network.converter.getCommonMedia
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class MediaServiceImpl(private val baseGraphRepository: BaseGraphRepository) :
    MediaService {
    override fun getMedia(
        field: MediaField,
        compositeDisposable: CompositeDisposable,
        callback: ((Resource<List<CommonMediaModel>>) -> Unit)
    ) {
        val disposable = baseGraphRepository.request(field.toQueryOrMutation())
            .map {
                it.data()?.Page()?.media()?.map {
                    it.fragments().narrowMediaContent().getCommonMedia(CommonMediaModel())
                }
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.invoke(Resource.success(it))
            }, {
                Timber.w(it)
                callback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })

        compositeDisposable.add(disposable)
    }

    override fun getSelectableMedia(
        field: MediaField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<List<SelectableCommonMediaModel>>) -> Unit
    ) {
        val disposable = baseGraphRepository.request(field.toQueryOrMutation())
            .map {
                it.data()?.Page()?.media()?.map {
                    it.fragments().narrowMediaContent().getCommonMedia(SelectableCommonMediaModel())
                }
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.invoke(Resource.success(it))
            }, {
                Timber.w(it)
                callback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })

        compositeDisposable.add(disposable)
    }
}