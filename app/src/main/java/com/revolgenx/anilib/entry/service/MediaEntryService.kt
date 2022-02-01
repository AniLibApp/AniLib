package com.revolgenx.anilib.entry.service

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.entry.data.model.EntryListEditorMediaModel
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.list.data.model.MediaListModel
import io.reactivex.disposables.CompositeDisposable

abstract class MediaEntryService(
    val context: Context,
    val graphRepository: BaseGraphRepository
) {
    abstract fun queryMediaListEntry(
        mediaId: Int?,
        compositeDisposable: CompositeDisposable? = null,
        callback: (Resource<MediaListModel>) -> Unit
    )

    abstract fun saveMediaListEntry(
        model: MediaListModel,
        compositeDisposable: CompositeDisposable? = null,
        callback: (Resource<MediaListModel>) -> Unit
    )

    abstract fun deleteMediaListEntry(
        listId: Int,
        compositeDisposable: CompositeDisposable? = null,
        callback: (Resource<Boolean>) -> Unit
    )

    abstract fun increaseProgress(
        model: MediaListModel,
        compositeDisposable: CompositeDisposable? = null,
        callback: (Resource<MediaListModel>) -> Unit
    )
}