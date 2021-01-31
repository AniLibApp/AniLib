package com.revolgenx.anilib.infrastructure.service.airing

import android.os.Handler
import com.revolgenx.anilib.data.field.home.AiringMediaField
import com.revolgenx.anilib.data.field.list.MediaListCollectionIdsField
import com.revolgenx.anilib.data.model.airing.AiringAtModel
import com.revolgenx.anilib.data.model.airing.TimeUntilAiringModel
import com.revolgenx.anilib.data.model.airing.AiringTimeModel
import com.revolgenx.anilib.data.model.airing.AiringMediaModel
import com.revolgenx.anilib.data.model.entry.MediaEntryListModel
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.network.converter.getCommonMedia
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.infrastructure.service.list.MediaListService
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
        callback: ((items: Resource<List<AiringMediaModel>>) -> Unit)
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
        callback: ((items: Resource<List<AiringMediaModel>>) -> Unit)
    ) {
        val disposable = baseGraphRepository.request(field.toQueryOrMutation())
            .map {
                it.data()?.Page()?.airingSchedules()
                    ?.filter {
                        if (field.canShowAdult) true else it.media()?.fragments()
                            ?.narrowMediaContent()?.isAdult == false
                    }
                    ?.map {
                        AiringMediaModel().also { model ->
                            model.airingTimeModel = AiringTimeModel().also { airingTimeModel ->
                                airingTimeModel.episode = it.episode()
                                airingTimeModel.timeUntilAiring =
                                    TimeUntilAiringModel().also { ti ->
                                        ti.time = it.timeUntilAiring().toLong()
                                    }

                                airingTimeModel.airingAt = AiringAtModel(
                                    LocalDateTime.ofInstant(
                                        Instant.ofEpochSecond(
                                            it.airingAt().toLong()
                                        ), ZoneOffset.systemDefault()
                                    )
                                )

                                CoroutineScope(Dispatchers.Main).launch {
                                    airingTimeModel.commonTimer =
                                        CommonTimer(Handler(), airingTimeModel.timeUntilAiring!!)
                                }
                            }
                            it.media()?.fragments()?.narrowMediaContent()?.mediaListEntry()?.let {
                                model.mediaEntryListModel = MediaEntryListModel(
                                    it.progress() ?: 0,
                                    it.status()?.ordinal
                                )
                            }


                            it.media()?.fragments()?.narrowMediaContent()?.getCommonMedia(model)
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