package com.revolgenx.anilib.service.airing

import android.os.Handler
import com.revolgenx.anilib.field.home.airing.AiringMediaField
import com.revolgenx.anilib.model.AiringTime
import com.revolgenx.anilib.model.AiringTimeModel
import com.revolgenx.anilib.model.airing.AiringMediaModel
import com.revolgenx.anilib.model.entry.MediaEntryListModel
import com.revolgenx.anilib.repository.network.BaseGraphRepository
import com.revolgenx.anilib.repository.network.converter.getCommonMedia
import com.revolgenx.anilib.repository.util.ERROR
import com.revolgenx.anilib.repository.util.Resource
import com.revolgenx.anilib.util.CommonTimer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

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
                    ?.filter { it.media()?.fragments()?.narrowMediaContent()?.isAdult == false }
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
                            model.mediaEntryListModel =
                                MediaEntryListModel(it.media()?.fragments()?.narrowMediaContent()?.mediaListEntry()?.let {
                                    it.progress() ?: 0
                                })
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