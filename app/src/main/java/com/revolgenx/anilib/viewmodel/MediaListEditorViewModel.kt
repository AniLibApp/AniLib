package com.revolgenx.anilib.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.exception.ApolloHttpException
import com.revolgenx.anilib.MediaListEditorQuery
import com.revolgenx.anilib.model.ListEditorMediaModel
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

class MediaListEditorViewModel(
    private val context: Context,
    private val repository: BaseGraphRepository
) : ViewModel() {
    val mediaQueryLiveData = MutableLiveData<Resource<ListEditorMediaModel>>()
    private val compositeDisposable = CompositeDisposable()

    fun queryMediaList(mediaId: Int) {
        mediaQueryLiveData.value = Resource.loading(null)
        val disposable = repository.request(
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
                mediaQueryLiveData.value = Resource.success(it)
            }, {
                if ((it is ApolloHttpException)) {
                    if (it.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                        mediaQueryLiveData.value = Resource.success(null)
                    } else {
                        mediaQueryLiveData.value = Resource.error(it.message ?: "Error", null)
                    }
                } else {
                    mediaQueryLiveData.value = Resource.error(it.message ?: "Error", null)
                }
            })
        compositeDisposable.add(disposable)
    }



    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}