package com.revolgenx.anilib.infrastructure.service.list

import com.revolgenx.anilib.app.setting.data.model.MediaListOptionModel
import com.revolgenx.anilib.data.field.list.*
import com.revolgenx.anilib.common.repository.network.BaseGraphRepository
import com.revolgenx.anilib.common.repository.network.converter.toModel
import com.revolgenx.anilib.common.repository.util.ERROR
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.list.data.model.MediaListModel
import com.revolgenx.anilib.media.data.model.toModel
import com.revolgenx.anilib.user.data.model.UserModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class MediaListServiceImpl(private val graphRepository: BaseGraphRepository) : MediaListService {

    //TODO check if can be removed from airing list
    override fun getMediaListCollectionIds(
        field: MediaListCollectionIdsField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<List<Int>>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data?.mediaListCollection?.lists?.mapNotNull {
                    it?.entries?.mapNotNull {
                        it?.takeIf { if (field.canShowAdult) true else it.media?.isAdult == false }?.media?.id
                    }
                }?.flatten()
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
                val userModel = it.data?.user?.let {
                    UserModel().also { userModel ->
                        userModel.mediaListOptions =
                            it.mediaListOptions?.let { option ->
                                MediaListOptionModel().also { optionModel ->
                                    optionModel.scoreFormat =
                                        option.scoreFormat?.ordinal
                                }
                            }
                    }
                }
                it.data?.page?.mediaList?.mapNotNull { list ->
                    list?.takeIf { if (field.canShowAdult) true else it.media?.mediaContent?.isAdult == false }
                        ?.let { mediaList ->
                            MediaListModel().also { model ->
                                model.id = mediaList.id
                                model.user = userModel

                                model.progress = mediaList.progress
                                model.score = mediaList.score
                                model.startedAt = mediaList.startedAt?.fuzzyDate?.toModel()
                                model.completedAt =
                                    mediaList.completedAt?.fuzzyDate?.toModel()
                                model.user = userModel
                                model.media = mediaList.media?.mediaContent?.toModel()
                            }
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