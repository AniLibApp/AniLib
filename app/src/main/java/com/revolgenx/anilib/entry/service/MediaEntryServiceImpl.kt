package com.revolgenx.anilib.entry.service

import android.content.Context
import com.apollographql.apollo3.exception.ApolloHttpException
import com.revolgenx.anilib.DeleteMediaListEntryMutation
import com.revolgenx.anilib.SaveMediaListEntryMutation
import com.revolgenx.anilib.common.preference.UserPreference
import com.revolgenx.anilib.entry.data.model.EntryListEditorMediaModel
import com.revolgenx.anilib.entry.data.model.AdvancedScoreModel
import com.revolgenx.anilib.common.preference.userScoreFormat
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.list.data.model.MediaListModel
import com.revolgenx.anilib.type.FuzzyDateInput
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.type.ScoreFormat
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.net.HttpURLConnection

class MediaEntryServiceImpl(context: Context, graphRepository: BaseGraphRepository) :
    MediaEntryService(context, graphRepository) {

    override fun queryMediaListEntry(
        mediaId: Int?,
        compositeDisposable: CompositeDisposable?,
        callback: (Resource<MediaListModel>) -> Unit
    ) {
//        val disposable = graphRepository.request(
//            MediaListEditorQuery.builder()
//                .mediaId(mediaId)
//                .userId(UserPreference.userId)
//                .format(ScoreFormat.values()[context.userScoreFormat()])
//                .build()
//        ).map { response ->
//            response.data()?.MediaList()!!.let { mediaList ->
//                mediaList.fragments().mediaListContent().toListEditorMediaModel()
//                    .also {
//                        it.score = mediaList.score()!!
//                        it.advancedScoring =
//                            (mediaList.advancedScores() as? Map<String, Double>)?.map {
//                                AdvancedScoreModel(it.key, it.value)
//                            }
//                        it.isUserList = true
//                    }
//            }
//        }.observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                callback.invoke(Resource.success(it))
//            }, {
//                if ((it is ApolloHttpException)) {
//                    if (it.code() == HttpURLConnection.HTTP_NOT_FOUND) {
//                        callback.invoke(Resource.success(null))
//                        return@subscribe
//                    }
//                }
//                Timber.w(it)
//                callback.invoke(Resource.error(it.message ?: "Error", null))
//            })
//
//        compositeDisposable?.add(disposable)
    }

    override fun saveMediaListEntry(
        model: MediaListModel,
        compositeDisposable: CompositeDisposable?,
        callback: (Resource<MediaListModel>) -> Unit
    ) {
//        val disposable = graphRepository.request(
//            SaveMediaListEntryMutation.builder().apply {
//                model.listId.takeIf { it != -1 }?.let {
//                    id(it)
//                }
//                mediaId(model.mediaId)
//
//                if (model.type == MediaType.MANGA.ordinal) {
//                    model.progressVolumes?.let {
//                        progressVolumes(model.progressVolumes)
//                    }
//                }
//                model.progress?.let {
//                    progress(it)
//                }
//                model.private?.let {
//                    private_(it)
//                }
//                model.status?.let {
//                    status(MediaListStatus.values()[it])
//                }
//                model.score?.let {
//                    score(it)
//                }
//                model.advancedScoring?.let {
//                    advanceScores(it.map { it.score })
//                }
//                model.repeat?.let {
//                    repeat(it)
//                }
//                model.notes?.let {
//                    notes(it)
//                }
//
//                model.startDate?.year?.let {
//                    startedAt(
//                        FuzzyDateInput.builder().day(model.startDate!!.day)
//                            .month(model.startDate!!.month).year(
//                                it
//                            ).build()
//                    )
//                } ?: startedAt(null)
//                model.endDate?.year?.let {
//                    completedAt(
//                        FuzzyDateInput.builder().day(model.endDate!!.day)
//                            .month(model.endDate!!.month).year(
//                                it
//                            ).build()
//                    )
//                } ?: completedAt(null)
//            }.build()
//        )
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                it.data()?.SaveMediaListEntry()?.let {
//                    callback.invoke(Resource.success(model.also { mod ->
//                        mod.progress = it.progress() ?: 0
//                        mod.progressVolumes = it.progressVolumes()
//                    }))
//                }
//            }, {
//                Timber.w(it)
//                callback.invoke(Resource.error(it.message ?: "Error", null))
//            })
//
//        compositeDisposable?.add(disposable)
    }


    override fun deleteMediaListEntry(
        listId: Int,
        compositeDisposable: CompositeDisposable?,
        callback: (Resource<Boolean>) -> Unit
    ) {
//        val disposable = graphRepository.request(
//            DeleteMediaListEntryMutation.builder().listId(listId).build()
//        )
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                val boolean = it.data()?.DeleteMediaListEntry()!!.deleted()
//                callback.invoke(Resource.success(boolean))
//            }, {
//                Timber.w(it)
//                callback.invoke(Resource.error(it.message ?: "Error", null))
//            })
//        compositeDisposable?.add(disposable)
    }

    override fun increaseProgress(
        model: MediaListModel,
        compositeDisposable: CompositeDisposable?,
        callback: (Resource<MediaListModel>) -> Unit
    ) {
//        val disposable = graphRepository.request(
//            SaveMediaListEntryMutation.builder().apply {
//                id(model.id)
//                model.mediaId.takeIf { it != -1 }?.let {
//                    mediaId(it)
//                }
//                progress(model.progress!!)
//            }.build()
//        ).observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                callback.invoke(Resource.success(model))
//            }, {
//                callback.invoke(Resource.error(it.message ?: ERROR, null, it))
//            })
//        compositeDisposable?.add(disposable)
    }
}