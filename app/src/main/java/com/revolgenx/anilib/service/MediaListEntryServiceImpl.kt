package com.revolgenx.anilib.service

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
import com.revolgenx.anilib.type.ScoreFormat
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.net.HttpURLConnection

class MediaListEntryServiceImpl(context: Context, graphRepository: BaseGraphRepository) :
    MediaListEntryService(context, graphRepository) {

    override fun queryMediaListEntry(
        mediaId: Int,
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
                } else {
                    mediaQueryEntryLiveData.value = Resource.error(it.message ?: "Error", null)
                }
            })

        compositeDisposable?.add(disposable)
        return mediaQueryEntryLiveData
    }

    override fun saveMediaListEntry(
        listId: Int?,
        mediaId: Int,
        compositeDisposable: CompositeDisposable?
    ): MutableLiveData<Resource<Int>> {
        val disposable = graphRepository.request(
            SaveMediaListEntryMutation.builder().listId(listId).mediaId(mediaId).build()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val id = it.data()?.SaveMediaListEntry()!!.id()
                saveMediaListEntryLiveData.value = Resource.success(id)
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