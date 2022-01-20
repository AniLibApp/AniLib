package com.revolgenx.anilib.list.service

import com.revolgenx.anilib.app.setting.data.model.MediaListOptionModel
import com.revolgenx.anilib.app.setting.data.model.MediaListOptionTypeModel
import com.revolgenx.anilib.app.setting.data.model.getRowOrder
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.network.converter.toModel
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.list.data.field.MediaListCollectionField
import com.revolgenx.anilib.list.data.model.MediaListCollectionModel
import com.revolgenx.anilib.list.data.model.MediaListGroupModel
import com.revolgenx.anilib.list.data.model.MediaListModel
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.user.data.model.UserModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class MediaListCollectionService(private val graphRepository: BaseGraphRepository) {
    private val filteredEntries by lazy {
        listOf(
            "Completed",
            "Dropped",
            "Planning",
            "Paused",
            "Watching",
            "Rewatching",
            "Rereading",
            "Reading"
        )
    }

    fun getMediaListCollection(
        field: MediaListCollectionField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<MediaListCollectionModel>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data()?.MediaListCollection()?.let { collection ->
                    MediaListCollectionModel().also { collectionModel ->
                        collectionModel.user = collection.user()?.let { user ->
                            UserModel().also { userModel ->
                                userModel.id = user.id()
                                userModel.mediaListOptions =
                                    user.mediaListOptions()?.let { option ->
                                        MediaListOptionModel().also { optionModel ->
                                            optionModel.scoreFormat = option.scoreFormat()?.ordinal
                                            optionModel.rowOrder = getRowOrder(option.rowOrder())
                                            optionModel.rowOrder = getRowOrder(option.rowOrder())
                                            optionModel.animeList =
                                                option.animeList()?.let { type ->
                                                    MediaListOptionTypeModel().also { typeModel ->
                                                        typeModel.customLists = type.customLists()
                                                        typeModel.sectionOrder = type.sectionOrder()
                                                    }
                                                }
                                            optionModel.mangaList =
                                                option.mangaList()?.let { type ->
                                                    MediaListOptionTypeModel().also { typeModel ->
                                                        typeModel.customLists = type.customLists()
                                                        typeModel.sectionOrder = type.sectionOrder()
                                                    }
                                                }
                                        }
                                    }
                            }
                        }

                        collectionModel.lists = collection.lists()?.map { group ->
                            MediaListGroupModel().also { groupModel ->
                                groupModel.name = group.name()
                                groupModel.isCustomList = group.isCustomList == true
                                groupModel.isCompletedList = group.isCompletedList == true
                                groupModel.entries = group.entries()?.map {
                                    it.fragments().mediaListEntry().let { list ->
                                        MediaListModel().also { model ->
                                            model.id = list.id()
                                            model.progress = list.progress()
                                            model.score = list.score()

                                            model.startedAt =
                                                list.startedAt()?.fragments()?.fuzzyDate()
                                                    ?.toModel()
                                            model.completedAt =
                                                list.completedAt()?.fragments()?.fuzzyDate()
                                                    ?.toModel()
                                            model.createdAt = list.createdAt()
                                            model.updatedAt = list.updatedAt()

                                            model.media = list.media()?.let { media ->
                                                MediaModel().also { modelModel ->
                                                    modelModel.id = media.id()
                                                    modelModel.episodes = media.episodes()
                                                    modelModel.chapters = media.chapters()
                                                    modelModel.title =
                                                        media.title()?.fragments()?.mediaTitle()
                                                            ?.toModel()
                                                    modelModel.coverImage =
                                                        media.coverImage()?.fragments()
                                                            ?.mediaCoverImage()?.toModel()
                                                    modelModel.bannerImage = media.bannerImage()
                                                        ?: modelModel.coverImage?.extraLarge
                                                    modelModel.startDate =
                                                        media.startDate()?.fragments()?.fuzzyDate()
                                                            ?.toModel()
                                                    modelModel.endDate =
                                                        media.endDate()?.fragments()?.fuzzyDate()
                                                            ?.toModel()
                                                    modelModel.averageScore = media.averageScore()
                                                    modelModel.popularity = media.popularity()
                                                    modelModel.type = media.type()?.ordinal
                                                    modelModel.genres = media.genres()
                                                    modelModel.format = media.format()?.ordinal
                                                    modelModel.status = media.status()?.ordinal
                                                    modelModel.synonyms = media.synonyms()
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }?.toMutableList()?.also {
                            val model = MediaListGroupModel()
                            model.name = "All"
                            model.entries = it.filter { f -> filteredEntries.contains(f.name) }
                                .flatMap { it.entries ?: emptyList() }
                            it.add(model)
                        }
                    }
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resourceCallback.invoke(Resource.success(it))
            }, {
                Timber.w(it)
                resourceCallback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })
        compositeDisposable.add(disposable)
    }
}