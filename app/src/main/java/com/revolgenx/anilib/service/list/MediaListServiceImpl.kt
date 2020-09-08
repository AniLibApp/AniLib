package com.revolgenx.anilib.service.list

import com.revolgenx.anilib.field.list.MediaListCollectionField
import com.revolgenx.anilib.field.list.MediaListField
import com.revolgenx.anilib.model.CoverImageModel
import com.revolgenx.anilib.model.TitleModel
import com.revolgenx.anilib.model.list.MediaListModel
import com.revolgenx.anilib.repository.network.BaseGraphRepository
import com.revolgenx.anilib.repository.network.converter.getCommonMedia
import com.revolgenx.anilib.repository.network.converter.toModel
import com.revolgenx.anilib.repository.util.ERROR
import com.revolgenx.anilib.repository.util.Resource
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
                                    model.title = media.title()?.userPreferred()?.let {
                                        TitleModel().apply { userPreferred = it }
                                    }
                                    model.coverImage = media.coverImage()?.large()?.let {
                                        CoverImageModel().apply { large = it }
                                    }
                                    model.startDate =
                                        media.startDate()?.fragments()?.fuzzyDate()?.toModel()
                                    model.averageScore = media.averageScore()
                                    model.popularity = media.popularity()
                                    model.type = field.type
                                    model.genres = media.genres()
                                    model.format = media.format()?.ordinal
                                    model.status = media.status()?.ordinal
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

    override fun getMediaList(
        field: MediaListField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<List<MediaListModel>>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data()?.Page()?.mediaList()?.filter { if(field.canShowAdult) true else it.media()?.fragments()?.narrowMediaContent()?.isAdult == false}?.map {
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
}