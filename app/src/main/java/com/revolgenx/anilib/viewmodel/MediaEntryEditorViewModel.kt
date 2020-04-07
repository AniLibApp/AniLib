package com.revolgenx.anilib.viewmodel

import android.content.Context
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.model.EntryListEditorMediaModel
import com.revolgenx.anilib.field.ToggleFavouriteField
import com.revolgenx.anilib.model.entry.MediaEntryListModel
import com.revolgenx.anilib.repository.util.Resource
import com.revolgenx.anilib.service.MediaListEntryService
import com.revolgenx.anilib.service.ToggleService
import io.reactivex.disposables.CompositeDisposable

class MediaEntryEditorViewModel(
    private val context: Context,
    private val mediaListEntryService: MediaListEntryService,
    private val toggleService: ToggleService
) : ViewModel() {
    val queryMediaListEntryLiveData by lazy {
        MediatorLiveData<Resource<EntryListEditorMediaModel>>().apply {
            addSource(mediaListEntryService.mediaQueryEntryLiveData) {
                this.value = it
            }
        }
    }
    val deleteMediaListEntryLiveData by lazy {
        MediatorLiveData<Resource<Boolean>>().apply {
            addSource(mediaListEntryService.deleteMediaListEntryLiveData) {
                this.value = it
            }
        }
    }
    val saveMediaListEntryLiveData by lazy {
        MediatorLiveData<Resource<Int>>().apply {
            addSource(mediaListEntryService.saveMediaListEntryLiveData) {
                this.value = it
            }
        }
    }
    val toggleFavMediaLiveData by lazy {
        MediatorLiveData<Resource<Boolean>>().apply {
            addSource(toggleService.toggleFavMutableLiveData) {
                this.value = it
            }
        }
    }

    private val compositeDisposable by lazy {
        CompositeDisposable()
    }

    fun queryMediaListEntry(mediaId: Int?) {
        queryMediaListEntryLiveData.value = Resource.loading(null)
        mediaListEntryService.queryMediaListEntry(
            mediaId,
            compositeDisposable
        )
    }

    fun saveMediaListEntry(model: EntryListEditorMediaModel) {
        saveMediaListEntryLiveData.value = Resource.loading(null)
        mediaListEntryService.saveMediaListEntry(
            model,
            compositeDisposable
        )
    }

    fun deleteMediaListEntry(listId: Int) {
        deleteMediaListEntryLiveData.value = Resource.loading(null)
        mediaListEntryService.deleteMediaListEntry(
            listId,
            compositeDisposable
        )
    }

    fun toggleMediaFavourite(toggleFavouriteField: ToggleFavouriteField) {
        toggleFavMediaLiveData.value = Resource.loading(null)
        toggleService.toggleFavourite(
            toggleFavouriteField,
            compositeDisposable
        )
    }

    fun isFavouriteQuery(mediaId: Int?): MutableLiveData<Resource<Boolean>> {
        return toggleService.isFavourite(mediaId, compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}

