package com.revolgenx.anilib.infrastructure.service.list

import com.revolgenx.anilib.data.field.list.MediaListCollectionField
import com.revolgenx.anilib.data.field.list.MediaListCollectionIdsField
import com.revolgenx.anilib.data.field.list.MediaListCountField
import com.revolgenx.anilib.data.field.list.MediaListField
import com.revolgenx.anilib.data.model.list.MediaListCountModel
import com.revolgenx.anilib.data.model.list.MediaListCountTypeModel
import com.revolgenx.anilib.data.model.list.MediaListModel
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.network.converter.getCommonMedia
import com.revolgenx.anilib.infrastructure.repository.network.converter.toModel
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.type.MediaType
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class MediaListServiceImpl(private val graphRepository: BaseGraphRepository) : MediaListService {
    override fun getMediaListCollection(
        field: MediaListCollectionField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<List<MediaListModel>>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                val mediaListCollection = mutableListOf<MediaListModel>()
                it.data()?.MediaListCollection()?.lists()?.forEach { list ->
                    list.entries()
                        ?.filter { if (field.canShowAdult) true else it.media()?.isAdult == false }
                        ?.map {
                            MediaListModel().also { model ->
                                model.mediaListId = it.id()
                                model.progress = it.progress()
                                model.score = it.score()

                                model.listStartDate =
                                    it.startedAt()?.fragments()?.fuzzyDate()?.toModel()
                                model.listCompletedDate =
                                    it.completedAt()?.fragments()?.fuzzyDate()?.toModel()
                                model.listUpdatedDate = it.updatedAt()
                                model.listCreatedDate = it.createdAt()

                                model.scoreFormat =
                                    it.user()?.mediaListOptions()?.scoreFormat()?.ordinal

                                it.media()?.let { media ->
                                    model.mediaId = media.id()
                                    model.episodes = media.episodes()?.toString()
                                    model.chapters = media.chapters()?.toString()
                                    model.title = media.title()?.fragments()?.mediaTitle()?.toModel()
                                    model.coverImage = media.coverImage()?.fragments()?.mediaCoverImage()?.toModel()
                                    model.bannerImage = media.bannerImage() ?: model.coverImage?.extraLarge
                                    model.startDate =
                                        media.startDate()?.fragments()?.fuzzyDate()?.toModel()
                                    model.endDate =
                                        media.endDate()?.fragments()?.fuzzyDate()?.toModel()
                                    model.averageScore = media.averageScore()
                                    model.popularity = media.popularity()
                                    model.type = field.type
                                    model.genres = media.genres()
                                    model.format = media.format()?.ordinal
                                    model.status = media.status()?.ordinal
                                    model.synonyms = media.synonyms()
                                }
                            }
                        }?.let { mediaList ->
                            mediaListCollection.addAll(mediaList)
                        }
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
                it.data()?.MediaListCollection()?.lists()?.forEach {
                    it.entries()
                        ?.filter { if (field.canShowAdult) true else it.media()?.isAdult == false }
                        ?.forEach { mediaIds.add(it.media()?.id()!!) }
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
        callback: (Resource<List<MediaListModel>>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data()?.Page()?.mediaList()?.filter {
                    if (field.canShowAdult) true else it.media()?.fragments()
                        ?.narrowMediaContent()?.isAdult == false
                }?.map {
                    MediaListModel().also { model ->
                        model.mediaListId = it.id()
                        model.progress = it.progress()
                        model.score = it.score()

                        model.listStartDate = it.startedAt()?.fragments()?.fuzzyDate()?.toModel()
                        model.listCompletedDate =
                            it.completedAt()?.fragments()?.fuzzyDate()?.toModel()
                        model.scoreFormat =
                            it.user()?.mediaListOptions()?.scoreFormat()?.ordinal
                        it.media()?.fragments()?.narrowMediaContent()?.getCommonMedia(model)
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


    override fun getMediaListCount(
        field: MediaListCountField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<List<MediaListCountTypeModel>>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {

                val mediaListCountTypeModels = mutableListOf<MediaListCountTypeModel>()
                it.data()?.User()?.statistics()?.let {
                    it.anime()?.let {
                        val listCountModels = mutableListOf<MediaListCountModel>()
                        it.statuses()?.forEach {
                            listCountModels.add(it.fragments().userListCount().toModel())
                        }
                        mediaListCountTypeModels.add(
                            MediaListCountTypeModel(
                                MediaType.ANIME.ordinal,
                                listCountModels
                            )
                        )
                    }
                    it.manga()?.let {
                        val listCountModels = mutableListOf<MediaListCountModel>()
                        it.statuses()?.forEach {
                            listCountModels.add(it.fragments().userListCount().toModel())
                        }
                        mediaListCountTypeModels.add(
                            MediaListCountTypeModel(
                                MediaType.MANGA.ordinal,
                                listCountModels
                            )
                        )
                    }
                    mediaListCountTypeModels
                }
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.invoke(Resource.success(it))
            }, {
                callback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })
        compositeDisposable.add(disposable)
    }
}