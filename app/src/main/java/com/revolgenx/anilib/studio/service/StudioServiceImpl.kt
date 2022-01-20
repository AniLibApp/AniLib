package com.revolgenx.anilib.studio.service

import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.network.converter.getCommonMedia
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.studio.data.field.StudioField
import com.revolgenx.anilib.studio.data.field.StudioMediaField
import com.revolgenx.anilib.studio.data.model.StudioMediaModel
import com.revolgenx.anilib.studio.data.model.StudioModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class StudioServiceImpl(private val graphRepository: BaseGraphRepository) : StudioService {


    override fun getStudioInfo(
        field: StudioField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<StudioModel>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data()?.Studio()?.let {
                    StudioModel().also { model ->
                        model.id = it.id()
                        model.studioName = it.name()
                        model.favourites = it.favourites()
                        model.isFavourite = it.isFavourite
                        model.siteUrl = it.siteUrl()
                    }
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

    override fun getStudioMedia(
        field: StudioMediaField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<List<StudioMediaModel>>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data()?.Studio()?.media()?.nodes()
                    ?.filter {
                        if (field.canShowAdult) true else it.fragments()
                            .commonMediaContent().isAdult == false
                    }?.map {
                        it.fragments().commonMediaContent().getCommonMedia(StudioMediaModel())
                    }
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resourceCallback.invoke(Resource.success(it))
            }, {
                Timber.w(it)
                resourceCallback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })

        compositeDisposable.add(disposable)
    }
}