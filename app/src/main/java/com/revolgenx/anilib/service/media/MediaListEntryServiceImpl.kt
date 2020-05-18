package com.revolgenx.anilib.service.media

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo.exception.ApolloHttpException
import com.revolgenx.anilib.DeleteMediaListEntryMutation
import com.revolgenx.anilib.MediaListEditorQuery
import com.revolgenx.anilib.SaveMediaListEntryMutation
import com.revolgenx.anilib.model.EntryListEditorMediaModel
import com.revolgenx.anilib.preference.userId
import com.revolgenx.anilib.preference.userScoreFormat
import com.revolgenx.anilib.repository.network.BaseGraphRepository
import com.revolgenx.anilib.repository.network.converter.toListEditorMediaModel
import com.revolgenx.anilib.repository.util.Resource
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
            response.data()?.MediaList()!!.fragments().mediaListContent().toListEditorMediaModel()
                .also {
                    it.score = response.data()?.MediaList()!!.score()!!
                }
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mediaQueryEntryLiveData.value = Resource.success(it)
            }, {
                if ((it is ApolloHttpException)) {
                    if (it.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                        mediaQueryEntryLiveData.value = Resource.success(null)
                    } else {
                        mediaQueryEntryLiveData.value = Resource.error(it.message ?: "Error", null)
                    }
                    Timber.w(it)
                } else {
                    mediaQueryEntryLiveData.value = Resource.error(it.message ?: "Error", null)
                    Timber.e(it)
                }
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
                    private_(model.private)
                }
                model.status?.let {
                    status(MediaListStatus.values()[it])
                }
                model.score?.let {
                    score(model.score)
                }
                model.repeat?.let {
                    repeat(model.repeat)
                }
                model.notes?.let {
                    notes(model.notes)
                }

                model.startDate?.year?.let {
                    startedAt(
                        FuzzyDateInput.builder().day(model.startDate!!.day).month(model.startDate!!.month).year(
                            it
                        ).build()
                    )
                } ?: startedAt(null)
                model.endDate?.year?.let {
                    startedAt(
                        FuzzyDateInput.builder().day(model.endDate!!.day).month(model.endDate!!.month).year(
                            it
                        ).build()
                    )
                } ?: completedAt(null)
            }.build()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.data()?.SaveMediaListEntry()?.let {
                    saveMediaListEntryLiveData.value = Resource.success(model.also {mod->
                        mod.progress = it.progress()
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

}