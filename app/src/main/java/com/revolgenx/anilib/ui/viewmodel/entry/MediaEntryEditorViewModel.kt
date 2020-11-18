package com.revolgenx.anilib.ui.viewmodel.entry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.data.field.ToggleFavouriteField
import com.revolgenx.anilib.data.model.EntryListEditorMediaModel
import com.revolgenx.anilib.data.model.MediaBrowseModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.service.ToggleService
import com.revolgenx.anilib.infrastructure.service.media.MediaBrowseService
import com.revolgenx.anilib.infrastructure.service.media.MediaListEntryService
import io.reactivex.disposables.CompositeDisposable

class MediaEntryEditorViewModel(
    private val mediaListEntryService: MediaListEntryService,
    private val mediaBrowseService: MediaBrowseService,
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
        MediatorLiveData<Resource<EntryListEditorMediaModel>>().apply {
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

    val mediaLiveData by lazy {
        MediatorLiveData<Resource<MediaBrowseModel>>().also {
            it.addSource(mediaBrowseService.simpleMediaLiveData) { res ->
                it.value = res
            }
        }
    }

    private val compositeDisposable by lazy {
        CompositeDisposable()
    }

    var apiModelEntry: EntryListEditorMediaModel = EntryListEditorMediaModel()

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

    fun getMediaInfo(mediaId: Int?): LiveData<Resource<MediaBrowseModel>> {
        return mediaBrowseService.getSimpleMedia(mediaId, compositeDisposable)
    }

    fun isFavouriteQuery(mediaId: Int?): MutableLiveData<Resource<Boolean>> {
        return toggleService.isFavourite(mediaId, compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}

