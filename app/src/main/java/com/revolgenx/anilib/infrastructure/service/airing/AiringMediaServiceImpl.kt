package com.revolgenx.anilib.infrastructure.service.airing

import android.os.Handler
import com.revolgenx.anilib.data.field.home.AiringMediaField
import com.revolgenx.anilib.data.model.AiringTime
import com.revolgenx.anilib.data.model.AiringTimeModel
import com.revolgenx.anilib.data.model.airing.AiringMediaModel
import com.revolgenx.anilib.data.model.entry.MediaEntryListModel
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.network.converter.getCommonMedia
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.util.CommonTimer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class AiringMediaServiceImpl(private val baseGraphRepository: BaseGraphRepository) :
    AiringMediaService {
    override fun getAiringMedia(
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
                                airingTimeModel.airingTime = AiringTime().also { ti ->
                                    ti.time = it.timeUntilAiring().toLong()
                                }

                                CoroutineScope(Dispatchers.Main).launch {
                                    airingTimeModel.commonTimer =
                                        CommonTimer(Handler(), airingTimeModel.airingTime!!)
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
                Timber.e(it)
                callback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })

        compositeDisposable.add(disposable)
    }
}