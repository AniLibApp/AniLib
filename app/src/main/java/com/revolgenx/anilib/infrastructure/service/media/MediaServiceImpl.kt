package com.revolgenx.anilib.infrastructure.service.media

import com.revolgenx.anilib.media.data.field.MediaField
import com.revolgenx.anilib.common.data.model.CommonMediaModel
import com.revolgenx.anilib.staff.data.model.StaffNameModel
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.network.converter.getCommonMedia
import com.revolgenx.anilib.infrastructure.repository.network.converter.toModel
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.staff.data.model.StaffConnectionModel
import com.revolgenx.anilib.staff.data.model.StaffEdgeModel
import com.revolgenx.anilib.staff.data.model.StaffModel
import com.revolgenx.anilib.studio.data.model.StudioConnectionModel
import com.revolgenx.anilib.studio.data.model.StudioEdgeModel
import com.revolgenx.anilib.studio.data.model.StudioModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class MediaServiceImpl(private val baseGraphRepository: BaseGraphRepository) : MediaService {
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
        callback: (Resource<List<MediaModel>>) -> Unit
    ) {
        val disposable = baseGraphRepository.request(field.toQueryOrMutation())
            .map {
                it.data()?.Page()?.media()?.map {
                    it.fragments().narrowMediaContent().toModel().also { model ->
                        model.studios = it.studios()?.let {
                            StudioConnectionModel().also { studioConnectionModel ->
                                studioConnectionModel.edges = it.edges()?.map { edge ->
                                    StudioEdgeModel().also { edgeModel ->
                                        edgeModel.isMain = edge.isMain
                                        edgeModel.node = edge.node()?.let { node ->
                                            StudioModel().also { studioModel ->
                                                studioModel.id = node.id()
                                                studioModel.studioName = node.name()
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        it.staff()?.let {
                            StaffConnectionModel().also { staffConnectionModel ->
                                staffConnectionModel.edges = it.edges()?.map { edge ->
                                    StaffEdgeModel().also { edgeModel ->
                                        edgeModel.role = edge.role()
                                        edgeModel.node = edge.node()?.let { node ->
                                            StaffModel().also { model ->
                                                model.id = node.id()
                                                model.name = node.name()?.let {
                                                    StaffNameModel().also { model ->
                                                        model.full = it.full()
                                                    }
                                                }
                                            }
                                        }
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
                callback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })

        compositeDisposable.add(disposable)
    }
}