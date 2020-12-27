package com.revolgenx.anilib.infrastructure.service.media

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo.exception.ApolloHttpException
import com.revolgenx.anilib.DeleteMediaListEntryMutation
import com.revolgenx.anilib.MediaListEditorQuery
import com.revolgenx.anilib.SaveMediaListEntryMutation
import com.revolgenx.anilib.data.model.EntryListEditorMediaModel
import com.revolgenx.anilib.data.model.entry.AdvancedScore
import com.revolgenx.anilib.common.preference.userId
import com.revolgenx.anilib.common.preference.userScoreFormat
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.network.converter.toListEditorMediaModel
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.type.FuzzyDateInput
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.type.ScoreFormat
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.net.HttpURLConnection

class MediaListEntryServiceImpl(context: Context, graphRepository: BaseGraphRepository) :
    MediaListEntryService(context, graphRepository) {

    @Suppress("UNCHECKED_CAST")
    override fun queryMediaListEntry(
        mediaId: Int?,
        compositeDisposable: CompositeDisposable?
    ): MutableLiveData<Resource<EntryListEditorMediaModel>> {
        val disposable = graphRepository.request(
            MediaListEditorQuery.builder()
                .mediaId(mediaId)
                .userId(context.userId())
                .format(ScoreFormat.values()[context.userScoreFormat()])
                .build()
        ).map { response ->
            response.data()?.MediaList()!!.let { mediaList ->
                mediaList.fragments().mediaListContent().toListEditorMediaModel()
                    .also {
                        it.score = mediaList.score()!!
                        it.advancedScoring =
                            (mediaList.advancedScores() as? Map<String, Double>)?.map {
                                AdvancedScore(it.key, it.value)
                            }
                        it.isUserList = true
                    }
            }
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mediaQueryEntryLiveData.value = Resource.success(it)
            }, {
                if ((it is ApolloHttpException)) {
                    if (it.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                        mediaQueryEntryLiveData.value = Resource.success(null)
                        return@subscribe
                    }
                }
                mediaQueryEntryLiveData.value = Resource.error(it.message ?: "Error", null)
                Timber.e(it)
            })

        compositeDisposable?.add(disposable)
        return mediaQueryEntryLiveData
    }

    override fun saveMediaListEntry(
        model: EntryListEditorMediaModel,
        compositeDisposable: CompositeDisposable?
    ): MutableLiveData<Resource<EntryListEditorMediaModel>> {
        val disposable = graphRepository.request(
            SaveMediaListEntryMutation.builder().apply {
                model.listId.takeIf { it != -1 }?.let {
                    listId(it)
                }
                mediaId(model.mediaId)

                if (model.type == MediaType.MANGA.ordinal) {
                    model.progressVolumes?.let {
                        progressVolumes(model.progressVolumes)
                    }
                }
                model.progress?.let {
                    progress(it)
                }
                model.private?.let {
                    private_(it)
                }
                model.status?.let {
                    status(MediaListStatus.values()[it])
                }
                model.score?.let {
                    score(it)
                }
                model.advancedScoring?.let {
                    advanceScores(it.map { it.score })
                }
                model.repeat?.let {
                    repeat(it)
                }
                model.notes?.let {
                    notes(it)
                }

                model.startDate?.year?.let {
                    startedAt(
                        FuzzyDateInput.builder().day(model.startDate!!.day)
                            .month(model.startDate!!.month).year(
                                it
                            ).build()
                    )
                } ?: startedAt(null)
                model.endDate?.year?.let {
                    completedAt(
                        FuzzyDateInput.builder().day(model.endDate!!.day)
                            .month(model.endDate!!.month).year(
                                it
                            ).build()
                    )
                } ?: completedAt(null)
            }.build()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.data()?.SaveMediaListEntry()?.let {
                    saveMediaListEntryLiveData.value = Resource.success(model.also { mod ->
                        mod.progress = it.progress() ?: 0
                        mod.progressVolumes = it.progressVolumes()
                    })
                }
            }, {
                Timber.e(it)
                saveMediaListEntryLiveData.value = Resource.error(it.message ?: "Error", null)
            })

        compositeDisposable?.add(disposable)
        return saveMediaListEntryLiveData
    }

    override fun deleteMediaListEntry(
        listId: Int,
        compositeDisposable: CompositeDisposable?
    ): MutableLiveData<Resource<Boolean>> {
        val disposable = graphRepository.request(
            DeleteMediaListEntryMutation.builder().listId(listId).build()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val boolean = it.data()?.DeleteMediaListEntry()!!.deleted()
                deleteMediaListEntryLiveData.value = Resource.success(boolean)
            }, {
                Timber.e(it)
                deleteMediaListEntryLiveData.value = Resource.error(it.message ?: "Error", null)
            })
        compositeDisposable?.add(disposable)
        return deleteMediaListEntryLiveData
    }

    override fun increaseProgress(
        model: EntryListEditorMediaModel,
        compositeDisposable: CompositeDisposable?,
        callback: (Resource<EntryListEditorMediaModel>) -> Unit
    ) {
        val disposable = graphRepository.request(
            SaveMediaListEntryMutation.builder().apply {
                listId(model.listId)
                model.mediaId?.let {
                    mediaId(it)
                }
                progress(model.progress!!)
            }.build()
        ).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.invoke(Resource.success(model))
            }, {
                callback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })
        compositeDisposable?.add(disposable)
    }
}