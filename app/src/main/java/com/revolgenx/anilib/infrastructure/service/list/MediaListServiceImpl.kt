package com.revolgenx.anilib.infrastructure.service.list

import com.revolgenx.anilib.data.field.list.*
import com.revolgenx.anilib.data.model.list.AlMediaListModel
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.network.converter.toModel
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

//TODO MEDIALIST SERVICE
class MediaListServiceImpl(private val graphRepository: BaseGraphRepository) : MediaListService {
    override fun getMediaListCollection(
        field: MediaListCollectionField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<List<AlMediaListModel>>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                val mediaListCollection = mutableListOf<AlMediaListModel>()
                it.data?.mediaListCollection?.lists?.filterNotNull()?.forEach { list ->
                    list.entries
                        ?.filter { if (field.canShowAdult) true else it?.media?.isAdult == false }
                        ?.filterNotNull()
//                        ?.map { it.toModel() }?.let { mediaList ->
//                            mediaListCollection.addAll(mediaList)
//                        }
                }
                mediaListCollection
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resourceCallback.invoke(Resource.success(it))
            }, {
                Timber.w(it)
                resourceCallback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })

        compositeDisposable.add(disposable)
    }

    override fun getMediaListCollectionIds(
        field: MediaListCollectionIdsField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<List<Int>>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                val mediaIds = mutableListOf<Int>()
                it.data?.mediaListCollection?.lists?.filterNotNull()?.forEach {
                    it.entries
                        ?.filter { if (field.canShowAdult) true else it?.media?.isAdult == false }
                        ?.filterNotNull()
                        ?.forEach { mediaIds.add(it.media?.id!!) }
                }
                mediaIds
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resourceCallback.invoke(Resource.success(it))
            }, {
                resourceCallback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })

        compositeDisposable.add(disposable)
    }

    override fun getMediaList(
        field: MediaListField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<List<AlMediaListModel>>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data?.page?.mediaList?.filterNotNull()?.filter {
                    if (field.canShowAdult) true else it.media?.mediaContent?.isAdult == false
                }?.map {
                    AlMediaListModel().also { model ->
                        model.mediaListId = it.id
                        model.progress = it.progress
                        model.score = it.score

                        model.listStartDate = it.startedAt?.fuzzyDate?.toModel()
                        model.listCompletedDate =
                            it.completedAt?.fuzzyDate?.toModel()
                        model.scoreFormat =
                            it.user?.mediaListOptions?.scoreFormat?.ordinal
//                        it.media?.fragments?.narrowMediaContent?.getCommonMedia(model)
                    }
                }
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.invoke(Resource.success(it))
            }, {
                Timber.e(it)
                callback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })

        compositeDisposable.add(disposable)
    }


}