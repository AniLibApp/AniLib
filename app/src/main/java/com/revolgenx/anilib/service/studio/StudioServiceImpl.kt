package com.revolgenx.anilib.service.studio

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.field.studio.StudioField
import com.revolgenx.anilib.field.studio.StudioMediaField
import com.revolgenx.anilib.model.CoverImageModel
import com.revolgenx.anilib.model.TitleModel
import com.revolgenx.anilib.model.entry.MediaEntryListModel
import com.revolgenx.anilib.model.studio.StudioMediaModel
import com.revolgenx.anilib.model.studio.StudioModel
import com.revolgenx.anilib.repository.network.BaseGraphRepository
import com.revolgenx.anilib.repository.network.converter.getCommonMedia
import com.revolgenx.anilib.repository.util.ERROR
import com.revolgenx.anilib.repository.util.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class StudioServiceImpl(private val graphRepository: BaseGraphRepository) : StudioService {
    override val studioInfoLivData: MutableLiveData<Resource<StudioModel>> by lazy {
        MutableLiveData<Resource<StudioModel>>()
    }

    override fun getStudioInfo(
        field: StudioField,
        compositeDisposable: CompositeDisposable
    ): MutableLiveData<Resource<StudioModel>> {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data()?.Studio()?.let {
                    StudioModel().also { model ->
                        model.studioId = it.id()
                        model.studioName = it.name()
                        model.favourites = it.favourites()
                        model.isFavourite = it.isFavourite
                        model.siteUrl = it.siteUrl()
                    }
                }
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                studioInfoLivData.value = Resource.success(it)
            }, {
                Timber.w(it)
                studioInfoLivData.value = Resource.error(it.message ?: ERROR, null, it)
            })

        compositeDisposable.add(disposable)

        return studioInfoLivData
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