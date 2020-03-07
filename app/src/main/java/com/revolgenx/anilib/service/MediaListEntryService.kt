package com.revolgenx.anilib.service

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.model.EntryListEditorMediaModel
import com.revolgenx.anilib.repository.network.BaseGraphRepository
import com.revolgenx.anilib.repository.util.Resource
import io.reactivex.disposables.CompositeDisposable

abstract class MediaListEntryService(
    val context: Context,
    val graphRepository: BaseGraphRepository
) {
    val mediaQueryEntryLiveData by lazy {
        MutableLiveData<Resource<EntryListEditorMediaModel>>()
    }
    val saveMediaListEntryLiveData by lazy {
        MutableLiveData<Resource<Int>>()
    }
    val deleteMediaListEntryLiveData by lazy {
        MutableLiveData<Resource<Boolean>>()
    }

    abstract fun queryMediaListEntry(
        mediaId: Int,
        compositeDisposable: CompositeDisposable? = null
    ): MutableLiveData<Resource<EntryListEditorMediaModel>>

    abstract fun saveMediaListEntry(
        model: EntryListEditorMediaModel,
        compositeDisposable: CompositeDisposable? = null
    ): MutableLiveData<Resource<Int>>

    abstract fun deleteMediaListEntry(
        listId: Int,
        compositeDisposable: CompositeDisposable? = null
    ): MutableLiveData<Resource<Boolean>>
}