package com.revolgenx.anilib.infrastructure.service.media

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.data.model.EntryListEditorMediaModel
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import io.reactivex.disposables.CompositeDisposable

abstract class MediaListEntryService(
    val context: Context,
    val graphRepository: BaseGraphRepository
) {
    val mediaQueryEntryLiveData by lazy {
        MutableLiveData<Resource<EntryListEditorMediaModel>>()
    }
    val saveMediaListEntryLiveData by lazy {
        MutableLiveData<Resource<EntryListEditorMediaModel>>()
    }
    val deleteMediaListEntryLiveData by lazy {
        MutableLiveData<Resource<Boolean>>()
    }

    abstract fun queryMediaListEntry(
        mediaId: Int?,
        compositeDisposable: CompositeDisposable? = null
    ): MutableLiveData<Resource<EntryListEditorMediaModel>>

    abstract fun saveMediaListEntry(
        model: EntryListEditorMediaModel,
        compositeDisposable: CompositeDisposable? = null
    ): MutableLiveData<Resource<EntryListEditorMediaModel>>

    abstract fun deleteMediaListEntry(
        listId: Int,
        compositeDisposable: CompositeDisposable? = null
    ): MutableLiveData<Resource<Boolean>>

    abstract fun increaseProgress(
        model: EntryListEditorMediaModel,
        compositeDisposable: CompositeDisposable? = null,
        callback: (Resource<EntryListEditorMediaModel>) -> Unit
    )
}