package com.revolgenx.anilib.airing.service

import android.os.Handler
import android.os.Looper
import com.revolgenx.anilib.airing.data.field.AiringMediaField
import com.revolgenx.anilib.airing.data.model.*
import com.revolgenx.anilib.data.field.list.MediaListCollectionIdsField
import com.revolgenx.anilib.entry.data.model.MediaEntryListModel
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.infrastructure.service.list.MediaListService
import com.revolgenx.anilib.list.data.model.MediaListModel
import com.revolgenx.anilib.media.data.model.toModel
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.util.CommonTimer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class AiringMediaServiceImpl(
    private val baseGraphRepository: BaseGraphRepository,
    private val mediaListService: MediaListService
) :
    AiringMediaService {
    override fun getAiringMedia(
        field: AiringMediaField,
        compositeDisposable: CompositeDisposable,
        callback: ((items: Resource<List<AiringScheduleModel>>) -> Unit)
    ) {

        if (field.showFromWatching || field.showFromPlanning) {
            if (field.isNewField || field.dataChanged) {
                mediaListService.getMediaListCollectionIds(MediaListCollectionIdsField().also {
                    it.userId = field.userId
                    it.userName = field.userName
                    it.mediaListStatus = field.mediaListStatus
                    it.type = MediaType.ANIME.ordinal
                }, compositeDisposable) {
                    when (it.status) {
                        Status.SUCCESS -> {
                            val data = it.data ?: return@getMediaListCollectionIds
                            field.isNewField = false
                            field.updateOldField()
                            field.mediaListIds = data
                            getAiringMediaList(field, compositeDisposable, callback)
                        }
                        Status.ERROR -> {
                            callback.invoke(Resource.error(it.message ?: ERROR, null, it.exception))
                        }
                        Status.LOADING -> {
                            callback.invoke(Resource.loading())
                        }
                    }
                }
            } else {
                getAiringMediaList(field, compositeDisposable, callback)
            }
            return
        }

        getAiringMediaList(field, compositeDisposable, callback)

    }

    private fun getAiringMediaList(
        field: AiringMediaField,
        compositeDisposable: CompositeDisposable,
        callback: ((items: Resource<List<AiringScheduleModel>>) -> Unit)
    ) {
        val disposable = baseGraphRepository.request(field.toQueryOrMutation())
            .map { response ->
                response.data?.page?.airingSchedules?.filter {
                    if (field.canShowAdult) true else it?.media?.mediaContent?.isAdult == false
                }?.mapNotNull {
                    it?.let {
                        AiringScheduleModel().also { aModel ->
                            aModel.timeUntilAiring = it.timeUntilAiring
                            aModel.timeUntilAiringModel = TimeUntilAiringModel().also { ti ->
                                ti.time = it.timeUntilAiring.toLong()
                            }
                            aModel.episode = it.episode
                            aModel.airingAt = it.airingAt
                            aModel.airingAtModel =  AiringAtModel(
                                LocalDateTime.ofInstant(
                                    Instant.ofEpochSecond(
                                        it.airingAt.toLong()
                                    ), ZoneOffset.systemDefault()
                                )
                            )

                            aModel.commonTimer =
                                CommonTimer(
                                    Handler(Looper.getMainLooper()),
                                    aModel.timeUntilAiringModel!!
                                )

                            aModel.media = it.media?.mediaContent?.toModel()
                            aModel.media?.let { mediaModel->
                                mediaModel.mediaListEntry = it.media?.mediaContent?.mediaListEntry?.let {
                                    MediaListModel().also { lModel->
                                        lModel.progress = it.progress ?: 0
                                        lModel.status = it.status?.ordinal
                                    }
                                }
                            }

                        }
                    }
                }
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.invoke(Resource.success(it ?: emptyList()))
            }, {
                Timber.w(it)
                callback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })
        compositeDisposable.add(disposable)
    }
}