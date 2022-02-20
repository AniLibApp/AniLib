package com.revolgenx.anilib.entry.service

import com.apollographql.apollo3.api.Optional
import com.revolgenx.anilib.DeleteMediaListEntryMutation
import com.revolgenx.anilib.app.setting.data.model.MediaListOptionModel
import com.revolgenx.anilib.app.setting.data.model.MediaListOptionTypeModel
import com.revolgenx.anilib.data.tuples.MutablePair
import com.revolgenx.anilib.entry.data.field.MediaListEntryField
import com.revolgenx.anilib.entry.data.field.SaveMediaListEntryField
import com.revolgenx.anilib.entry.data.model.UserMediaModel
import com.revolgenx.anilib.common.repository.network.BaseGraphRepository
import com.revolgenx.anilib.common.repository.network.converter.toModel
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.list.data.model.MediaListModel
import com.revolgenx.anilib.list.data.model.toModel
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.media.data.model.toModel
import com.revolgenx.anilib.user.data.model.UserModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class MediaListEntryService(private val graphRepository: BaseGraphRepository) {
    fun getMediaListEntry(
        field: MediaListEntryField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<UserMediaModel>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation()).map {
            it.data?.let { data ->
                UserMediaModel().also { userMediaModel ->
                    userMediaModel.user = data.user?.let { user ->
                        UserModel().also { userModel ->
                            userModel.id = user.id
                            userModel.mediaListOptions = user.mediaListOptions?.let { option ->
                                MediaListOptionModel().also { optionModel ->
                                    optionModel.scoreFormat = option.scoreFormat?.ordinal
                                    optionModel.animeList = option.animeList?.let { optionType ->
                                        MediaListOptionTypeModel().also { optionTypeModel ->
                                            optionTypeModel.advancedScoring =
                                                optionType.advancedScoring?.filterNotNull()
                                                    ?.toMutableList()
                                            optionTypeModel.advancedScoringEnabled =
                                                optionType.advancedScoringEnabled == true
                                            optionTypeModel.customLists =
                                                optionType.customLists?.filterNotNull()
                                        }
                                    }
                                    optionModel.mangaList = option.mangaList?.let { optionType ->
                                        MediaListOptionTypeModel().also { optionTypeModel ->
                                            optionTypeModel.advancedScoring =
                                                optionType.advancedScoring?.filterNotNull()
                                                    ?.toMutableList()
                                            optionTypeModel.advancedScoringEnabled =
                                                optionType.advancedScoringEnabled == true
                                            optionTypeModel.customLists =
                                                optionType.customLists?.filterNotNull()
                                        }
                                    }
                                }
                            }
                        }
                    }
                    userMediaModel.media = data.media?.let { media ->
                        MediaModel().also { mediaModel ->
                            mediaModel.id = media.id
                            mediaModel.status = media.status?.ordinal
                            mediaModel.chapters = media.chapters
                            mediaModel.volumes = media.volumes
                            mediaModel.episodes = media.episodes
                            mediaModel.title = media.title?.mediaTitle?.toModel()
                            mediaModel.type = media.type?.ordinal
                            mediaModel.isFavourite = media.isFavourite
                            mediaModel.mediaListEntry =
                                media.mediaListEntry?.mediaListEntry?.toModel()?.also { entry ->
                                    entry.advanceScores =
                                        (media.mediaListEntry.advancedScores as? Map<String, *>)?.map {
                                            MutablePair(
                                                it.key,
                                                it.value?.let { if (it is Int) it.toDouble() else it as Double }
                                                    ?: 0.0
                                            )
                                        }
                                }

                        }
                    }
                }
            }
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.invoke(Resource.success(it))
            }, {
                Timber.w(it)
                callback.invoke(Resource.error(it.message, null, it))
            })
        compositeDisposable.add(disposable)
    }

    fun saveMediaListEntry(
        field: SaveMediaListEntryField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<MediaListModel>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data?.saveMediaListEntry?.let { listEntry ->
                    listEntry.mediaListEntry.toModel().also { entry ->
                        entry.media = listEntry.media?.mediaContent?.toModel()
                    }
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.invoke(Resource.success(it))
            }, {
                Timber.w(it)
                callback.invoke(Resource.error(it.message, null, it))
            })
        compositeDisposable.addAll(disposable)
    }

    fun deleteMediaListEntry(
        id: Int?,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<Boolean>) -> Unit
    ) {
        val disposable = graphRepository.request(
            DeleteMediaListEntryMutation(
                listId = Optional.presentIfNotNull(id)
            )
        )
            .map { it.data?.deleteMediaListEntry?.deleted }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.invoke(Resource.success(it))
            }, {
                Timber.w(it)
                callback.invoke(Resource.error(it.message, null, it))
            })

        compositeDisposable.add(disposable)
    }
}